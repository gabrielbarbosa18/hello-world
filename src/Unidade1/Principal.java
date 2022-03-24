package Unidade1;

import java.sql.*;

 class Carro {
	private int retrovisores;
	private String nome;
	private String marca;
	private float velocidade ;
	
	public Carro() {
		this.retrovisores = 0;
		this.nome = "";
		this.marca = "";
		this.velocidade = 0;
	}
	
	public Carro(int retrovisores, String nome, String marca, float velocidade) {
		this.retrovisores = retrovisores;
		this.nome = nome;
		this.marca = marca;
		this.velocidade = velocidade;
	}

	public int getRetrovisores() {
		return retrovisores;
	}

	public void setRetrovisores(int retrovisores) {
		this.retrovisores = retrovisores;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public float getVelocidade() {
		return velocidade;
	}

	public void setVelocidade(char velocidade) {
		this.velocidade = velocidade;
	}

	@Override
	public String toString() {
		return "Carro [retrovisores=" + retrovisores + ", nome=" + nome + ", senha=" + marca + ", velocidade=" + velocidade + "]";
	}	
}
 class DAO {
	private Connection conexao;
	
	public DAO() {
		conexao = null;
	}
	
	public boolean conectar() {
		String driverName = "org.postgresql.Driver";                    
		String serverName = "localhost";
		String mydatabase = "teste";
		int porta = 5432;
		String url = "jdbc:postgresql://" + serverName + ":" + porta +"/" + mydatabase;
		String username = "ti2cc";
		String password = "ti@cc";
		boolean status = false;

		try {
			Class.forName(driverName);
			conexao = DriverManager.getConnection(url, username, password);
			status = (conexao == null);
			System.out.println("ConexÃ£o efetuada com o postgres!");
		} catch (ClassNotFoundException e) { 
			System.err.println("ConexÃ£o NÃƒO efetuada com o postgres -- Driver nÃ£o encontrado -- " + e.getMessage());
		} catch (SQLException e) {
			System.err.println("ConexÃ£o NÃƒO efetuada com o postgres -- " + e.getMessage());
		}

		return status;
	}
	
	public boolean close() {
		boolean status = false;
		
		try {
			conexao.close();
			status = true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return status;
	}
	
	public boolean inserirCarro(Carro carro) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("INSERT INTO carro (retrovisores, nome, marca, velocidade) "
					       + "VALUES ("+carro.getRetrovisores()+ ", '" + carro.getNome() + "', '"  
					       + carro.getMarca() + "', '" + carro.getVelocidade() + "');");
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	public boolean atualizarCarro(Carro carro) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			String sql = "UPDATE carro SET nome = '" + carro.getNome() + "', marca = '"  
				       + carro.getMarca() + "', velocidade = '" + carro.getVelocidade() + "'"
					   + " WHERE retrovisores = " + carro.getRetrovisores();
			st.executeUpdate(sql);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	public boolean excluirCarro(int retrovisores) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM carro WHERE retrovisores = " + retrovisores);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	
	public Carro[] getCarros() {
		Carro[] carros = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("SELECT * FROM carro");		
	         if(rs.next()){
	             rs.last();
	             carros = new Carro[rs.getRow()];
	             rs.beforeFirst();

	             for(int i = 0; rs.next(); i++) {
	                carros[i] = new Carro(rs.getInt("retrovisores"), rs.getString("nome"), 
	                		                  rs.getString("marca"), rs.getString("velocidade").charAt(0));
	             }
	          }
	          st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return carros;
	}

	
	
}
 public class Principal {
		
		public static void main(String[] args) {
			
			DAO dao = new DAO();
			
			dao.conectar();

			
			//Inserir um elemento na tabela
			Carro carro = new Carro(2, "punto", "fiat",180.0);
			if(dao.inserirCarro(carro) == true) {
				System.out.println("Insercao com sucesso -> " + carro.toString());
			}
			

			//Atualizar carro
			carro.setMarca("ford");
			dao.atualizarCarro(carro);

			
			//Excluir carro
			dao.excluirCarro(carro.getCodigo());
			
			//Mostrar carros
			carros = dao.getCarros();
			System.out.println("==== Mostrar carros === ");		
			for(int i = 0; i < carros.length; i++) {
				System.out.println(carros[i].toString());
			}
			
			dao.close();
		}
	}
