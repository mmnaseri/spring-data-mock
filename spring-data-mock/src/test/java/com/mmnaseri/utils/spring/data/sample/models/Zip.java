package com.mmnaseri.utils.spring.data.sample.models;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/21/15)
 */
@SuppressWarnings("unused")
public class Zip implements Comparable<Zip> {

    private String prefix;
    private Long region;
    private String area;

    public String getPrefix() {
        return prefix;
    }

    public Zip setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public Long getRegion() {
        return region;
    }

    public Zip setRegion(Long region) {
        this.region = region;
        return this;
    }

    public String getArea() {
        return area;
    }

    public Zip setArea(String area) {
        this.area = area;
        return this;
    }

    @Override
    public int compareTo(Zip o) {
        return prefix.compareTo(o.prefix);
    }

}
