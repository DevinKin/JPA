package com.devinkin.jpa.helloworld;

import javax.persistence.*;

@Table(name = "JPA_MANAGERS")
@Entity
public class Manager {
    private Integer id;
    private String mgrName;
    private Department dept;

    @GeneratedValue
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "MGR_NAME")
    public String getMgrName() {
        return mgrName;
    }

    public void setMgrName(String mgrName) {
        this.mgrName = mgrName;
    }

    // 对于不维护关联关系，没有外键的一方，使用@OneToOne来进行映射，建议设置mapped=true
    @OneToOne(mappedBy = "manager")
    public Department getDept() {
        return dept;
    }

    public void setDept(Department dept) {
        this.dept = dept;
    }
}
