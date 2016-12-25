package eet.pospichal.eet.model;

import android.support.annotation.Nullable;

import com.siimkinks.sqlitemagic.annotation.Id;
import com.siimkinks.sqlitemagic.annotation.Table;

import org.jetbrains.annotations.NotNull;

/**
 * Created by pospile on 16/12/2016.
 */

@Table(persistAll = true)
public class Category {

    @Id(autoIncrement = true)
    public long id;

    public String jmeno;

}