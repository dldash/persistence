package com.dldash.persistence.contracts;

import java.util.List;

public interface Query {

    String sql();

    List<Object> bindings();

}
