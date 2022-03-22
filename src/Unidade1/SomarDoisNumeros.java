package Unidade1;
import java.util.*;
class SomarDoisNumeros {
	public static void main (String args[]){
		Scanner sc = new Scanner(System.in);
		//Declaracao de variaveis
		int num1, num2, soma;
		//Leituras
		System.out.println("Digite um número:\n");
		num1 = sc.nextInt();
		System.out.println("Digite outro número:\n");
		num2 = sc.nextInt();
		//Somar
		soma = num1 + num2;
		//Mostrar na tela
		System.out.println("Soma:" + soma);
		}

}
