package eet.pospichal.eet.model;

import android.support.annotation.Nullable;

import com.siimkinks.sqlitemagic.annotation.Id;
import com.siimkinks.sqlitemagic.annotation.Table;

import org.jetbrains.annotations.NotNull;

/**
 * Created by pospile on 16/12/2016.
 */

@Table(persistAll = true)
public class Product {

    @Id(autoIncrement = true)
    public long id;

    public String jmeno_produktu;

    public int cena_koruny;

    public int cena_halere;

    public int dan;

    public int inter_id;

    public String barcode;

    public int category;
}