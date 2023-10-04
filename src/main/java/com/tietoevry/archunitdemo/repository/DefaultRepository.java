package com.tietoevry.archunitdemo.repository;

import com.tietoevry.archunitdemo.service.DefaultApiService;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultRepository {

    private DefaultApiService defaultApiService = new DefaultApiService();
}
