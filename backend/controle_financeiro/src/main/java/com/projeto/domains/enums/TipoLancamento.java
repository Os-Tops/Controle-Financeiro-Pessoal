package com.projeto.domains.enums;

public enum TipoLancamento {

    PAGAR (0, "PAGAR"), RECEBER (1, "RECEBER");
    private Integer id;
    private String descricao;

    TipoLancamento(Integer id, String descricao) {
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

    public static TipoLancamento toEnum(Integer id){
        if(id == null) return null;
        for(TipoLancamento tipoLancamento : TipoLancamento.values()){
            if(id.equals(tipoLancamento.getId())){
                return tipoLancamento;
            }
        }
        throw new IllegalArgumentException("Tipo de Lançamento inválido!");
    }
}
