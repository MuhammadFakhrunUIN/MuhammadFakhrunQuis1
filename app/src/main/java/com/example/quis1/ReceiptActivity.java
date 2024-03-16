package com.example.quis1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import androidx.core.app.ShareCompat;
import android.widget.Button;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class ReceiptActivity extends AppCompatActivity {
    private TextView welcomeTextView, memberTextView, transaksiTextView,
            kodeBarangTextView, namaBarangTextView, hargaBarangTextView,
            totalHargaTextView, diskonHargaTextView, diskonMemberTextView,
            jumlahBayarTextView, terimaKasihTextView;
    private HashMap<String, Barang> daftarBarang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        // Init element UI
        welcomeTextView = findViewById(R.id.textViewWelcome);
        memberTextView = findViewById(R.id.textViewTipeMember);
        transaksiTextView = findViewById(R.id.textViewTransaksiTitle);
        kodeBarangTextView = findViewById(R.id.textViewKodeBarang);
        namaBarangTextView = findViewById(R.id.textViewNamaBarang);
        hargaBarangTextView = findViewById(R.id.textViewHargaBarang);
        totalHargaTextView = findViewById(R.id.textViewTotalHarga);
        diskonHargaTextView = findViewById(R.id.textViewDiskonHarga);
        diskonMemberTextView = findViewById(R.id.textViewDiskonMember);
        jumlahBayarTextView = findViewById(R.id.textViewJumlahBayar);
        terimaKasihTextView = findViewById(R.id.textViewTerimaKasih);

        // Init data barang
        daftarBarang = new HashMap<>();
        daftarBarang.put("LV3", new Barang("Lenovo V14 Gen 3", 6666666));
        daftarBarang.put("AA5", new Barang("Acer Aspire 5", 9999999));
        daftarBarang.put("MP3", new Barang("Macbook Pro M3", 28999999));

        // Mengambil data yang dikirimkan dari FormActivity
        Intent intent = getIntent();
        String nama = intent.getStringExtra("nama");
        String tipeMember = intent.getStringExtra("tipe_member");
        String kodeBarang = intent.getStringExtra("KODE_BARANG");
        int jumlahBarang = intent.getIntExtra("JUMLAH_BARANG", 0);

        // Menampilkan data di TextView
        welcomeTextView.setText("Selamat datang, " + nama + "!");
        memberTextView.setText("Tipe Member: " + tipeMember);

        // Menampilkan detail barang
        Barang barang = daftarBarang.get(kodeBarang);
        if (barang != null) {
            kodeBarangTextView.setText("Kode Barang: " + kodeBarang);
            namaBarangTextView.setText("Nama Barang: " + barang.getNama());
            hargaBarangTextView.setText("Harga Barang: " + formatRupiah(barang.getHarga()));

            // Kalkulasi total harga
            int totalHarga = barang.getHarga() * jumlahBarang;
            totalHargaTextView.setText("Total Harga: " + formatRupiah(totalHarga));

            // Kalkulasi diskon fix (100) & diskon member
            double diskonSeratus = 0;
            if (totalHarga >= 10000000) {
                diskonSeratus = 100000; // Diskon Rp100.000 jika transaksi di atas Rp10.000.000
            }
            double diskonMember = 0;
            if (tipeMember.equalsIgnoreCase("Gold")) {
                diskonMember = totalHarga * 0.1; // Diskon 10% untuk member Gold
            } else if (tipeMember.equalsIgnoreCase("Silver")) {
                diskonMember = totalHarga * 0.05; // Diskon 5% untuk member Silver
            } else if (tipeMember.equalsIgnoreCase("Biasa")) {
                diskonMember = totalHarga * 0.02; // Diskon 2% untuk member Biasa
            }
            diskonHargaTextView.setText("Diskon Harga: " + formatRupiah(diskonSeratus));
            diskonMemberTextView.setText("Diskon Member: " + formatRupiah(diskonMember));

            // Kalkulasi harga post-diskon
            double hargaSetelahDiskon = totalHarga - diskonSeratus - diskonMember;
            jumlahBayarTextView.setText("Jumlah Bayar: " + formatRupiah(hargaSetelahDiskon));

            // Error handling
        } else {
            Log.e("ReceiptActivity", "Kode barang tidak valid: " + kodeBarang);
        }

        // Init tombol share
        Button shareButton = findViewById(R.id.buttonShare);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Panggil fungsi untuk berbagi struk pembayaran
                shareReceipt();
            }
        });
    }

    // Struk pembayaran (receipt bill)
    private void shareReceipt() {
        String namaBarang = namaBarangTextView.getText().toString();
        String jumlahBayar = jumlahBayarTextView.getText().toString();

        String receiptText = "Struk Pembayaran:\n" +
                "Nama barang:\n" +
                namaBarang + "\n" +
                "Melakukan transaksi sebesar " + jumlahBayar + " di Muhammad Fakhrun Quis 1 :v";

        // Proc share
        ShareCompat.IntentBuilder
                .from(this)
                .setType("text/plain")
                .setChooserTitle("Bagikan Struk Pembayaran")
                .setText(receiptText)
                .startChooser();
    }

    // Class data barang
    private static class Barang {
        private String nama;
        private int harga;

        private Barang(String nama, int harga) {
            this.nama = nama;
            this.harga = harga;
        }
        private String getNama() {
            return nama;
        }
        private int getHarga() {
            return harga;
        }
    }

    // Format Rupiah
    private String formatRupiah(double nominal) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("Rp #,###", symbols);
        return formatter.format(nominal);
    }
}
