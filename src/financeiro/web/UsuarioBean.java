package financeiro.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name="usuarioBean")
@RequestScoped
public class UsuarioBean {
	private String nome;
	private String email;
	private String senha;
	private String confirmaSenha;
	
	public String getNome() {
		return nome;
	}
	
	public String novo(){
		return "usuario";
	}
	
	public String salvar(){
		FacesContext context = FacesContext.getCurrentInstance();
		if(!this.senha.equalsIgnoreCase(this.confirmaSenha)){
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Senha confirmada incorretamente", ""));
			return "usuario";
		} 
		try { 
			Class.forName("org.postgresql.Driver"); 
		} catch (ClassNotFoundException e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "DATABASE Error 5364", ""));
			return "usuario";
		}
 
		Connection connection = null;
 
		try { 
			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/book", "postgres",
					"admin"); 
		} catch (SQLException e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "DATABASE Error 5364", ""));
			return "usuario"; 
		}
 
		if (connection != null) {
			PreparedStatement insert = null;
			String sql = "INSERT INTO usuarios(nome, email, senha) VALUES(?,?,?)";
			try {
				insert = connection.prepareStatement(sql);
				insert.setString(1, this.nome);
				insert.setString(2, this.email);
				insert.setString(3, this.senha);
				insert.executeUpdate();
			} catch (SQLException e) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "DATABASE Error 5364", ""));
				return "usuario"; 
			} finally {
				try {
					insert.close();
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastrado com sucesso!", ""));
			return "mostraUsuario";
		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "DATABASE Error 5364", ""));
			return "usuario";
		}
		
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
	
	public String getConfirmaSenha() {
		return confirmaSenha;
	}
	
	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}
}
