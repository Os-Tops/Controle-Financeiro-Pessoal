package com.projeto.domains.enums;

public enum Status {
    ATIVA (0, "ATIVA"), INATIVA (1, "INATIVA");
    private Integer id;
    private String descricao;

    Status(Integer id, String descricao) {
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

    public static Status toEnum(Integer id){
        if(id == null) return null;
        for(Status status : Status.values()){
            if(id.equals(status.getId())){
                return status;
            }
        }
        throw new IllegalArgumentException("Status inválido!");
    }
}