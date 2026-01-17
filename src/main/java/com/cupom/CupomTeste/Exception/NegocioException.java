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

	public TabelaDeErros getErro() {
		// Encontra o enum correspondente ao código de erro
		for (TabelaDeErros erro : TabelaDeErros.values()) {
			if (erro.getCodigoDeErro().equals(this.codigoDeErro)) {
				return erro;
			}
		}
		return TabelaDeErros.ERRO_INTERNO;
	}
}