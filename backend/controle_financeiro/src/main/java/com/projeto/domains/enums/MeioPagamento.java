package com.projeto.domains.enums;

public enum MeioPagamento {

    CONTA (0, "CONTA"), CARTAO (1, "CARTAO"), DINHEIRO (2, "DINHEIRO"), PIX (3, "PIX");
    private Integer id;
    private String descricao;

    MeioPagamento(Integer id, String descricao) {
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
        throw new IllegalArgumentException("Meio de Pagamento inválido!");
    }
}
