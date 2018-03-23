package com.wzf.wucarryme.modules.main.domain;

import java.util.Date;

/**
 * @author wangzf
 * @date 2017/6/2
 */
public class Boss implements Comparable {
    private String name;
    private String bankuai;
    private int count = 1;
    private Date date;

    public Boss(String name, Date date) {
        this.name = name;
        this.date = date;
    }

    public Boss(String name, String bankuai, Date date) {
        this.name = name;
        this.bankuai = bankuai;
        this.date = date;
    }

    public String getBankuai() {
        return bankuai;
    }

    public void setBankuai(String bankuai) {
        this.bankuai = bankuai;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void addCount() {
        this.count++;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        if (this.date.compareTo(date) < 0) {
            this.date = date;
        }
    }


    @Override
    public int compareTo(Object o) {
        Boss other = ((Boss) o);
        if (this.count > other.count) {
            return 1;
        } else if (this.count == other.count) {
            if (this.date.compareTo(other.date) > 0) {
                return 1;
            }
            if (this.date.equals(other.date)) {
                return 0;
            }
        }
        return -1;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Boss) obj).getName().equals(this.name);
    }

    @Override
    public String toString() {
        return this.name + "*" + this.count + "@" + this.date;
    }
}
