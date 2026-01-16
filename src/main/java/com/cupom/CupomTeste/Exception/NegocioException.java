package com.cupom.CupomTeste.Exception;

import org.springframework.http.HttpStatus;

public class NegocioException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final HttpStatus codigoHttp;
    private final String codigoDeErro;
    private final String mensagem;

    public NegocioException(TabelaDeErros tabela) {
        super(tabela.getMensagem()); // mensagem da superclasse
        this.codigoHttp = tabela.getCodigoHttp();
        this.codigoDeErro = tabela.getCodigoDeErro();
        this.mensagem = tabela.getMensagem();
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

	public Object getErro() {
		// TODO Auto-generated method stub
		return null;
	}
}