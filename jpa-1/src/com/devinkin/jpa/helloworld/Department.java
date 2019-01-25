package com.devinkin.jpa.helloworld;

import javax.persistence.*;

@Table(name = "JPA_DEPARTMENTS")
@Entity
public class Department {
    private Integer id;
    private String deptName;
    private Manager manager;

    // 使用@OneToOne 来映射1-1关联关系
    // 若需要在当前数据表中添加主键，则需要使用@JoinColumn来进行映射
    // 注意：1-1关联关系，所以需要添加 unique=true
    @JoinColumn(name = "MGR_ID", unique = true)
    @OneToOne(fetch = FetchType.LAZY)
    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }


    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "DEPT_NAME")
    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }


}
