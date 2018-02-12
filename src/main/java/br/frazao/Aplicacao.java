package br.frazao;

import br.frazao.dominio.Baralho;
import br.frazao.dominio.Fundo;
import br.frazao.dominio.Numero;

public class Aplicacao {

	public static void main(String[] args) {

		Baralho b1 = Baralho.criar(Fundo.PRETO, true);
		b1.embaralha();

		Baralho b2 = Baralho.criar(b1.descarta().get());

		b1.encarta(b2.descarta(b2.getCartas(Numero.DAMA)).get());

		b2.descarta(b2.getCartas(-3));

		b2.ordena();

		b2.descarta(b2.getCartas(23));

		System.out.println(b1);
		System.out.println(b2);

	}

}
