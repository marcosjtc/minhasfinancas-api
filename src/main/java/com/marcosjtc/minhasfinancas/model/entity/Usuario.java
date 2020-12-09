package com.marcosjtc.minhasfinancas.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Faz com que o JPA reconheça a classe como um mapeamento de uma entidade
@Entity
//Definição da tabela
@Table(name = "usuario", schema = "financas")
//@Setter
//@Getter
//@EqualsAndHashCode
//@ToString
@NoArgsConstructor //Construtor vazio
@AllArgsConstructor //Construtor com todas as propriedades 
@Data
@Builder
public class Usuario {
	
	@Id //Informa que este Id é a chave primária.
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Informa que o campo é autoincrementado. 
	//Auto é utilizado no mysql
	private Long id;
	
	@Column(name = "nome")
	private String nome;
	
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "senha")
	private String senha;
	
 /*	public static void main(String args[]) {
		Usuario usuario =  new Usuario();
		usuario.setEmail("marcos@gmail.com");
		usuario.setNome("Marcos");
		usuario.setSenha("senha");
		
		//Usuario.builder().nome("Marcos").email("email@gmail.com").build();
	} */

 /*	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((senha == null) ? 0 : senha.hashCode());
		return result;
	}

	//Auxilia na comparação de objetos e instâncias e com listas na procura de elementos.
	//Usa internamente o método para fazer comparação nas listas.
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (senha == null) {
			if (other.senha != null)
				return false;
		} else if (!senha.equals(other.senha))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nome=" + nome + ", email=" + email + ", senha=" + senha + "]";
	}
	
    */
	
}
