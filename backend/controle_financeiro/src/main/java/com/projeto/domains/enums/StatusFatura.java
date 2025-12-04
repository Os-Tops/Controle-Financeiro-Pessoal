package com.projeto.domains.enums;

public enum StatusFatura {
    ABERTA (0, "ABERTA"), FECHADA (1, "FECHADA"), PAGA (1, "PAGA");
    private Integer id;
    private String descricao;

    StatusFatura(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public static StatusFatura toEnum(Integer id){
        if(id == null) return null;
        for(StatusFatura statusFatura : StatusFatura.values()){
            if(id.equals(statusFatura.getId())){
                return statusFatura;
            }
        }
        throw new IllegalArgumentException("Status inválido!");
    }
}
