/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.web.template.IF;

/**
 *
 * @author tuanp
 * @param <T>
 */
public interface BasicIF<T> {

    public T findById(int id);

    public int create(T one);

    public T update(T one);

    public T delete(int id);

}
