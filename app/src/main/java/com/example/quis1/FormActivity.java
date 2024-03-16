package com.example.quis1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;

public class FormActivity extends AppCompatActivity {
    private EditText namaEditText, kodeBarangEditText, jumlahBarangEditText;
    private RadioButton radioButtonGold, radioButtonSilver, radioButtonBiasa;
    private RadioGroup radioGroup;
    private Button processButton;
    private HashMap<String, String> daftarBarang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        // Init elemen UI
        namaEditText = findViewById(R.id.editTextNama);
        kodeBarangEditText = findViewById(R.id.editTextKodeBarang);
        jumlahBarangEditText = findViewById(R.id.editTextJumlahBarang);
        radioButtonGold = findViewById(R.id.radioButtonGold);
        radioButtonSilver = findViewById(R.id.radioButtonSilver);
        radioButtonBiasa = findViewById(R.id.radioButtonBiasa);
        radioGroup = findViewById(R.id.radioGroupTipePelanggan);
        processButton = findViewById(R.id.buttonProcess);

        // Init data barang (3 buah)
        daftarBarang = new HashMap<>();
        daftarBarang.put("LV3", "Lenovo V14 Gen 3");
        daftarBarang.put("AA5", "Acer Aspire 5");
        daftarBarang.put("MP3", "Macbook Pro M3");

        // Menambahkan OnClickListener
        processButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidInput()) {
                    // Melanjutkan ke ReceiptActivity
                    Intent intent = new Intent(FormActivity.this, ReceiptActivity.class);
                    intent.putExtra("nama", namaEditText.getText().toString().trim());
                    intent.putExtra("tipe_member", getSelectedMemberType());
                    intent.putExtra("KODE_BARANG", kodeBarangEditText.getText().toString().trim());
                    intent.putExtra("JUMLAH_BARANG", Integer.parseInt(jumlahBarangEditText.getText().toString().trim()));
                    startActivity(intent);
                }
            }
        });
    }

    // Memeriksa validitas input
    private boolean isValidInput() {
        String nama = namaEditText.getText().toString().trim();
        String kodeBarang = kodeBarangEditText.getText().toString().trim();
        String jumlahBarangStr = jumlahBarangEditText.getText().toString().trim();

        if (TextUtils.isEmpty(nama)) {
            Toast.makeText(this, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(kodeBarang)) {
            Toast.makeText(this, "Kode barang tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!daftarBarang.containsKey(kodeBarang)) {
            Toast.makeText(this, "Kode barang tidak valid", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(jumlahBarangStr)) {
            Toast.makeText(this, "Jumlah barang tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validasi jumlah barang (numeric only)
        int jumlahBarang;
        try {
            jumlahBarang = Integer.parseInt(jumlahBarangStr);
            if (jumlahBarang <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Jumlah barang harus berupa angka yang lebih besar dari 0", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Menentukan tipe member yang dipilih (radio)
    private String getSelectedMemberType() {
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId == radioButtonGold.getId()) {
            return "Gold";
        } else if (selectedRadioButtonId == radioButtonSilver.getId()) {
            return "Silver";
        } else if (selectedRadioButtonId == radioButtonBiasa.getId()) {
            return "Biasa";
        }
        return "";
    }
}
