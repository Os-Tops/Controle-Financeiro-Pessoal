package com.projeto.domains.enums;

public enum StatusCartao {
    DESBLOQUEADO (0, "DESBLOQUEADO"), BLOQUEADO (1, "BLOQUEADO");
    private Integer id;
    private String descricao;

    StatusCartao(Integer id, String descricao) {
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

    public static StatusCartao toEnum(Integer id){
        if(id == null) return null;
        for(StatusCartao statusCartao : StatusCartao.values()){
            if(id.equals(statusCartao.getId())){
                return statusCartao;
            }
        }
        throw new IllegalArgumentException("Status do cartão inválido!");
    }
}
