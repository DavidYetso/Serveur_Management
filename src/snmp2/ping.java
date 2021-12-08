package snmp2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class ping {

	public static int main(String args) {
		StringTokenizer scan = new StringTokenizer (args, "/");
		args = scan.nextToken();
		System.out.println("Adresse testée = " + args);

		Process p = null;
		String commande = "ping -n 4 -w 1000 " + args;
		System.out.println("Commande testée = " + commande);
		BufferedReader bfIn = null;
		try  {
			p=Runtime.getRuntime().exec(commande);
			if (p == null)  {
				System.out.println("** Erreur d'exécution de la commande **"); return -1;
			}
			bfIn = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String strLine;
			boolean pasDeReponse = false;
			while ((strLine = bfIn.readLine()) != null) {
				System.out.println(strLine); // pour trace
				if (Trouve100(strLine)) {
					System.out.println("La machine " + args + " ne répond pas"); return 0;
				}
			}
			bfIn.close();
			System.out.println("La machine " + args + " a répondu");
			return 1;
		}
		catch(IOException e) { System.out.println("Exception IO = " + e.getMessage()); }
		catch(Exception e) { System.out.println("Exception = " + e.getMessage()); }
		return -1;
	}

	static boolean Trouve100 (String s) {
		boolean trouve = false;
		StringTokenizer scan = new StringTokenizer (s, " ");
		int cpt = 0;
		while (scan.hasMoreTokens()) {
			String essai = scan.nextToken();
			int pp = essai.indexOf("%");
			if (pp != -1) {
				int p100 = essai.indexOf("100");
				trouve = (p100 != -1);
			}
			if (trouve) return true;
		}
		return false;
	}
}
