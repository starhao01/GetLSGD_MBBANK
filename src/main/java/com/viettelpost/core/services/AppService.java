package com.viettelpost.core.services;

import java.util.List;

public interface AppService {


    void insertApp(String name, String desription, String Code) throws Exception;

    void deleteApp(Long id);

    void updateApp(String name, String description, String Code, Long id) throws Exception;

    int getTotalApp(String txtSearch) throws Exception;


}
