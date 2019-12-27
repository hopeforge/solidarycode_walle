package com.indracompany.walle.enums;

public enum MensagesEnum {	
	
	MENSAGEM_INICIAL(1, "Seja bem vindo ao Robô de Doações do GRAACC! Somos uma instituição sem fins lucrativos com o objetivo de garantir a crianças e adolescentes com câncer todas as chances de cura. Para acompanhar o nosso trabalho, acesse https://youtu.be/EcMDbN43nCY. Para acompanhar a prestação de contas, acesse https://graacc.org.br/quemsomos/prestacao-de-contas/. Prezamos pela transparência financeira. Gostou do nosso trabalho? Faça parte do nosso "
			+ "time nos ajudando a manter a saúde financeira da nossa instituição."
			+ " Para doar R$20,00, digite 20 e envie. Para doar R$35,00, digite 35 e envie. Para doar R$50,00, digite 50 e envie. Para doar R$150,00, digite 150 e envie.");
	
	private Integer id;
	private String text;
	
	private MensagesEnum(Integer id, String text) {
		this.id = id;
		this.text = text;
	}
	
	

}
