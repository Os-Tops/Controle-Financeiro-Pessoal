package com.projeto.domains.enums;

public enum StatusLancamento {

    PENDENTE(0, "PENDENTE"),
    BAIXADO(1, "BAIXADO"),
    PARCIAL(2, "PARCIAL"),
    CANCELADO(3, "CANCELADO");

    private Integer id;
    private String descricao;

    StatusLancamento(Integer id, String descricao) {
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
        throw new IllegalArgumentException("Status de Lançamento inválido!");
    }
}