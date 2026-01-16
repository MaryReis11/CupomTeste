package com.cupom.CupomTeste.Exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum TabelaDeErros {

    // Erros de validação do cupom
    CODIGO_INVALIDO(HttpStatus.BAD_REQUEST, "2001-1000", "O código do cupom deve conter exatamente 6 caracteres alfanuméricos"),
    VALOR_DESCONTO_INVALIDO(HttpStatus.BAD_REQUEST, "2001-1001", "O valor de desconto do cupom deve ser no mínimo 0.5"),
    DATA_EXPIRACAO_INVALIDA(HttpStatus.BAD_REQUEST, "2001-1002", "A data de expiração do cupom não pode estar no passado"),
    
    // Erros de operações
    CUPOM_NAO_ENCONTRADO(HttpStatus.NOT_FOUND, "2001-2000", "Cupom não encontrado"),
    CUPOM_JA_DELETADO(HttpStatus.PRECONDITION_FAILED, "2001-2001", "O cupom já foi deletado"),
    
    // Erro genérico
    ERRO_INTERNO(HttpStatus.INTERNAL_SERVER_ERROR, "2001-5000", "Erro interno no servidor");

    private final HttpStatus codigoHttp;
    private final String codigoDeErro;
    private final String mensagem;

    TabelaDeErros(HttpStatus codigoHttp, String codigoDeErro, String mensagem) {
        this.codigoHttp = codigoHttp;
        this.codigoDeErro = codigoDeErro;
        this.mensagem = mensagem;
    }

	public HttpStatus getCodigoHttp() {
		return codigoHttp;
	}

	public String getCodigoDeErro() {
		return codigoDeErro;
	}

	public String getMensagem() {
		return mensagem;
	}
}