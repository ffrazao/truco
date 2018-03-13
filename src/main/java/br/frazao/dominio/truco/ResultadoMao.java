package br.frazao.dominio.truco;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ResultadoMao {

	private List<Boolean> cangouList;

	private List<List<JogadorTruco>> disputaList;

	private Map<Set<JogadorTruco>, Integer> rankingTimeMap;

	private Integer tentos;

	private Set<JogadorTruco> timeVencedor;

	List<Boolean> getCangouList() {
		if (this.cangouList == null) {
			this.cangouList = new ArrayList<>();
		}
		return this.cangouList;
	}

	List<List<JogadorTruco>> getDisputaList() {
		if (this.disputaList == null) {
			this.disputaList = new ArrayList<>();
		}
		return this.disputaList;
	}

	Map<Set<JogadorTruco>, Integer> getRankingTimeMap() {
		if (this.rankingTimeMap == null) {
			this.rankingTimeMap = new LinkedHashMap<>();
		}
		return this.rankingTimeMap;
	}

	public Integer getTentos() {
		return tentos;
	}

	public Set<JogadorTruco> getTimeVencedor() {
		return timeVencedor;
	}

	void setTentos(Integer tentos) {
		this.tentos = tentos;
	}

	void setTimeVencedor(Set<JogadorTruco> timeVencedor) {
		this.timeVencedor = timeVencedor;
	}

}
