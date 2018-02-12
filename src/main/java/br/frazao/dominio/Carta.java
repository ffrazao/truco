package br.frazao.dominio;

import java.util.Objects;
import java.util.Optional;

public class Carta implements Comparable<Carta> {

	public static Carta criar(Fundo fundo, Numero numero, Naipe naipe) {
		return new Carta(fundo, numero, naipe);
	}

	private Fundo fundo;

	private Naipe naipe;

	private Numero numero;

	private Integer peso;

	private Carta(Fundo fundo, Numero numero, Naipe naipe) {
		super();
		this.fundo = Objects.requireNonNull(fundo);
		if (numero == null ^ naipe == null) {
			throw new IllegalStateException();
		}
		this.numero = numero;
		this.naipe = naipe;
	}

	@Override
	public int compareTo(Carta carta) {
		return Integer.compare(this.getOrdem(), carta.getOrdem());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Carta other = (Carta) obj;
		if (this.fundo != other.fundo)
			return false;
		if (this.naipe != other.naipe)
			return false;
		if (this.numero != other.numero)
			return false;
		return true;
	}

	public Fundo getFundo() {
		return this.fundo;
	}

	public Optional<Naipe> getNaipe() {
		return Optional.ofNullable(this.naipe);
	}

	public Optional<Numero> getNumero() {
		return Optional.ofNullable(this.numero);
	}

	public int getOrdem() {
		return this.isCoringa() ? 0 : (((this.getNaipe().get().getOrdem() - 1) * Numero.values().length) + this.getNumero().get().getOrdem());
	}

	public Integer getPeso() {
		return peso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fundo == null) ? 0 : fundo.hashCode());
		result = prime * result + ((naipe == null) ? 0 : naipe.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		return result;
	}

	public boolean isCoringa() {
		return this.numero == null && this.naipe == null;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}

	@Override
	public String toString() {
		return this.isCoringa() ? String.format("Coringa fundo %s ordem %d", this.fundo, this.getOrdem()) : String.format("%s de %s fundo %s ordem %d", this.numero, this.naipe, this.fundo, this.getOrdem());
	}

}
