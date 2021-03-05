import java.util.Collections;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
//progetto backup 02.06.19
public class Carattere {
	static Scanner inputScanner = new Scanner(System.in);
	static boolean continua=true;
	Random random;
	String carattere;

	Carattere(LinkedList<String> lista_caratteri){
		Collections.shuffle(lista_caratteri);
		carattere = lista_caratteri.get(0);
		random = new Random();
	}
	
	static private double Round(double numero) { 
		numero *= 100;
		float parteintera = (float)numero;
		int uscita = Math.round(parteintera);
		return (double)uscita/100;
	}
	
	public int input(int messaggio, Giocatore mittente, Giocatore self) {
		int interpretazione = 0;
		switch (carattere) {
		case "player":
			interpretazione = Player_input(messaggio, mittente, self);
			break;
		case "malvagio":
			interpretazione = Malvagio_input(messaggio, mittente, self);
			break;
		case "neutrale":
			interpretazione = neutrale_input(messaggio, mittente, self);
			break;
		case "positivo":
			interpretazione = Positivo_input(messaggio, mittente, self);
			break;
		case "random":
			interpretazione = Random_input(messaggio, mittente, self);
			break;
		case "negativo":
			interpretazione = Negativo_input(messaggio, mittente, self);
			break;
		case "bipolare":
			interpretazione = Bipolare_input(messaggio, mittente, self);
			break;
		case "rancoroso":
			interpretazione = Rancoroso_input(messaggio, mittente, self);
			break;
		case "visionario":
			interpretazione = Visionario_input(messaggio, mittente, self);
			break;
		default:
			break;
		}
		return interpretazione;
	}

	public int output(Giocatore destinatario, Giocatore self) {
		int messaggio = 0;
		switch (carattere) {
		case "player":
			messaggio = Player_output(destinatario, self);
			break;
		case "malvagio":
			messaggio = Malvagio_output(destinatario, self);
			break;
		case "neutrale":
			messaggio = neutrale_output(destinatario, self);
			break;
		case "bipolare":
			messaggio = Bipolare_output(destinatario, self);
			break;
		case "positivo":
			messaggio = Positivo_output(destinatario, self);
			break;
		case "random":
			messaggio = Random_output(destinatario, self);
			break;
		case "negativo":
			messaggio = Negativo_output(destinatario, self);
			break;
		case "rancoroso":
			messaggio = Rancoroso_output(destinatario, self);
			break;
		case "visionario":
			messaggio = Visionario_output(destinatario, self);
			break;
		default:
			break;
		}
		return messaggio;
	}

	public void AggiornaImportanza(Giocatore conoscente, Giocatore self) {
		switch (carattere) {
		case "player":
			Player_AggiornaImportanza(conoscente, self);
			break;
		case "malvagio":
			Malvagio_AggiornaImportanza(conoscente, self);
			break;
		case "neutrale":
			neutrale_AggiornaImportanza(conoscente, self);
			break;
		case "bipolare":
			Bipolare_AggiornaImportanza(conoscente, self);
			break;
		case "positivo":
			Positivo_AggiornaImportanza(conoscente, self);
			break;
		case "random":
			Random_AggiornaImportanza(conoscente, self);
			break;
		case "negativo":
			Negativo_AggiornaImportanza(conoscente, self);
			break;
		case "rancoroso":
			Rancoroso_AggiornaImportanza(conoscente, self);
			break;
		case "visionario":
			Visionario_AggiornaImportanza(conoscente, self);
			break;
		default:
			break;
		}
	}

	private int Random_input(int messaggio, Giocatore mittente, Giocatore self) {
		//questo metodo prende in input un numero da 0 a 100 che se conosce ne legge il significato e poi quest'ultimo
		//in base all'umore e anche allo storico volendo, viene alterato (incrementato o decrementato)
		//e quindi restituiamo l'interpretazione del messaggio
		//se non si conosce la parola ricevuta allora si attribuisce un significato random da -5 a 5 e la parola con il suo significato
		//viene messa nel vocabolario e nel dizionario_out viene aggiunta quella parola nella lista delle parole con quel significato
		//queste dichiarazioni sono uguali per ogni carattere input
		
		//LinkedList<Integer> storico = self.interazioni.get(mittente).storico;
		//double importanza = self.interazioni.get(mittente).importanza;
		//double umore = self.umore;
		int significato;
		if (self.vocabolario.containsKey(messaggio) == false) {
			significato = random.nextInt(11)-5;
			self.vocabolario.put(messaggio, significato);
			self.dizionario_out.get(significato).add(messaggio);
		}
		else significato = self.vocabolario.get(messaggio);
		//qui inizia l'algoritmo dove significato e' il significato del messaggio appena ricevuto e in base al carattere decrementiamo e incrementiamo il significato
		//qui finisce l'algoritmo e restituiamo il significato
		return significato;
	}

	private int Random_output(Giocatore destinatario, Giocatore self) {
		//questo metodo guardando lo storico (dove sono presenti tutti i messaggi che mi ha inviato colui a qui ora devo rispondere)
		//guardando l'imporanza (che indica quanto e' importante per me colui a cui devo rispondere)
		//guardando il mio umore in questo preciso momento
		//genero un valore da -5 a 5 che e' il significato del mesaggio che voglio inviare
		//e quindi gli invio come messaggio una parola che conosco (da 0 a 100) che vale quel significato
		//queste dichiarazioni sono uguali per ogni carattere output
		//double umore = self.umore;
		//double importanza = self.interazioni.get(destinatario).importanza;
		//LinkedList<Integer> storico = self.interazioni.get(destinatario).storico;
		HashMap<Integer, LinkedList<Integer>> dizionario_out = self.dizionario_out;
		//qui si scrive l'algoritmo specifico per ogni carattere output affinche' produca una variabile significato tra -5 e 5
		int significato = random.nextInt(11)-5;
		//qui finisce l'algoritmo specifico per ogni carattere output
		//queste due ultime righe sono comuni per ogni carattere output
		//che preso il significato che si vuole inviare viene restituita una parola che conosciamo con quel significato
		Collections.shuffle(dizionario_out.get(significato));
		return dizionario_out.get(significato).get(0);
	}

	private void Random_AggiornaImportanza(Giocatore conoscente, Giocatore self) {
		//qui consultando lo storico ((dove sono presenti tutti i significati dei messaggi che mi ha inviato il mio conoscente)
		//e consultando l'importanza, in base al mio carattere aggiorno l'importanza
		//questi parametri sono comuni a tutti i caratteri
		//LinkedList<Integer> storico = self.interazioni.get(conoscente).storico;
		double importanza = self.interazioni.get(conoscente).importanza;
		//qui inizia l'algoritmo che ci restituisce la nuova importanza del nostro conoscente per noi
		importanza += random.nextDouble()-0.5;
		//qui finisce e poi l'ultimo rigo e' comune a tutti dove l'importanza viene aggiornata con quella nuova
		self.interazioni.get(conoscente).importanza = importanza;
	}

	private void neutrale_AggiornaImportanza(Giocatore conoscente, Giocatore self) {
		//non fa nulla
	}

	private int neutrale_input(int messaggio, Giocatore mittente, Giocatore self) {
		int significato;
		if (self.vocabolario.containsKey(messaggio) == false) {
			significato = random.nextInt(11)-5;
			self.vocabolario.put(messaggio, significato);
			self.dizionario_out.get(significato).add(messaggio);
		}
		else significato = self.vocabolario.get(messaggio);
		return significato;
	}

	private int neutrale_output(Giocatore destinatario, Giocatore self) {
		HashMap<Integer, LinkedList<Integer>> dizionario_out = self.dizionario_out;
		Collections.shuffle(dizionario_out.get(0));
		return dizionario_out.get(0).get(0);
	}

	private void Positivo_AggiornaImportanza(Giocatore conoscente, Giocatore self) {
		LinkedList<Integer> storico = self.interazioni.get(conoscente).storico;
		double importanza = self.interazioni.get(conoscente).importanza;
		int pos=0;
		int neg=0;
		for (int i = 0; i < storico.size(); i++) {
			if (storico.get(i)>2) pos++;
			else if (storico.get(i)<=-2) neg++;
			if (i==5) break;
		}
		if (importanza>=4) {
			if (pos>=neg && importanza<=4.7) {
				importanza += 0.3;
				if (importanza>4.7) importanza=5;
			}
			else if (pos<neg) importanza -= 0.1;
		}
		else {
			if (pos>=neg) importanza += 0.5;
			else importanza -= 0.3;
		}
		self.interazioni.get(conoscente).importanza = importanza;
	}

	private int Positivo_input(int messaggio, Giocatore mittente, Giocatore self) {
		//in base al messaggio ricevuto ne attribuisce il vero significato (che prende dal vocabolario), poi in base ai parametri incrementa o decrementa il peso
		LinkedList<Integer> storico = self.interazioni.get(mittente).storico;
		double umore = self.umore;    	
		int significato;
		int pos=0;
		int neg=0;
		if (self.vocabolario.containsKey(messaggio) == false) {
			significato = random.nextInt(11)-5;
			self.vocabolario.put(messaggio, significato);
			self.dizionario_out.get(significato).add(messaggio);
		}
		else significato = self.vocabolario.get(messaggio);
		if (umore<=30) {
			for (int i = 0; i < storico.size(); i++) {
				if (storico.get(i)>2) pos++;
				else if (storico.get(i) <= -2) neg++;
				if (i==5) break;
			}
			if(pos>=neg) significato++;
		}
		if (30<umore && umore<=60) significato++;
		if (umore>60) significato+=2;
		return significato;
	}

	private int Positivo_output(Giocatore destinatario, Giocatore self) {
		//in base ai suoi parametri genera il peso del messaggio da inviare e ritorna un vocabolo (del dizionario) che ha quel peso
		LinkedList<Integer> storico = self.interazioni.get(destinatario).storico;
		double importanza = self.interazioni.get(destinatario).importanza;
		double umore = self.umore;
		HashMap<Integer, LinkedList<Integer>> dizionario_out = self.dizionario_out;

		int significato=0;
		int pos=0;
		int neg=0;
		for (int i = 0; i < storico.size(); i++) {
			if (storico.get(i)>2) pos++;
			else if (storico.get(i)<-2) neg++;
			if (i==5) break;
		}
		if (importanza>=4) {
			if (umore>30) significato=random.nextInt(3)+2;
			else {
				significato=random.nextInt(8)-3;
				if (significato<5 && pos>=neg) significato++;
			}
		}
		else {
			if (umore>30) significato=random.nextInt(7)-2;
			else { 
				significato=random.nextInt(9)-4;
				if (significato<5 && pos>=neg) significato++;
				else significato=5;
			}
		}
		Collections.shuffle(dizionario_out.get(significato));
		return dizionario_out.get(significato).get(0);
	}

	private int Negativo_input(int messaggio, Giocatore mittente, Giocatore self){
		LinkedList<Integer> storico = self.interazioni.get(mittente).storico;
		double umore = self.umore;
		int significato;
		int pos=0;
		int neg=0;
		if (self.vocabolario.containsKey(messaggio) == false) {
			significato = random.nextInt(11)-5;
			self.vocabolario.put(messaggio, significato);
			self.dizionario_out.get(significato).add(messaggio);
		}
		else significato = self.vocabolario.get(messaggio);
		//controllo sui parametri dello storico, in questo caso lo guarda sempre
		for (int i = 0; i < storico.size(); i++) {
			if (storico.get(i)>2) pos++;
			else if (storico.get(i)<=-2) neg++;
		}
		if (umore >= 50) {
			if(significato<0 || pos<=neg) significato--;
		}
		else {
			if(significato<0 || pos<=neg) significato -= 2;
			else significato--;
		}
		return significato;
	}

	private int Negativo_output(Giocatore destinatario, Giocatore self) {
		double umore = self.umore;
		double importanza = self.interazioni.get(destinatario).importanza;
		LinkedList<Integer> storico = self.interazioni.get(destinatario).storico;
		HashMap<Integer, LinkedList<Integer>> dizionario_out = self.dizionario_out;
		int pos=0;
		int neg=0;
		int significato=0;
		for (int i = 0; i < storico.size(); i++) {
			if (storico.get(i)>2) pos++;
			else if (storico.get(i)<-2) neg++;
		}
		if (umore >= 60 && pos > neg){
			if (importanza>=4 ){
				significato = random.nextInt(2)+4;
			}
			else significato = random.nextInt(2)+2;
		}
		if(umore >= 60 && pos <= neg) significato = random.nextInt(2);
		if (umore>=30 && umore <60){
			if (importanza>3.5){
				significato = random.nextInt(2)-1;
			}
			else significato= -3;
		}
		if (umore<30) significato = random.nextInt(2)-5;
		Collections.shuffle(dizionario_out.get(significato));
		return dizionario_out.get(significato).get(0);
	}

	private void Negativo_AggiornaImportanza(Giocatore conoscente, Giocatore self) {
		LinkedList<Integer> storico = self.interazioni.get(conoscente).storico;
		double importanza = self.interazioni.get(conoscente).importanza;
		int pos=0;
		int neg=0;
		for (int i=0; i<storico.size(); i++) {
			if (storico.get(i) > 2) pos++;
			else neg++;
			if (i==5) break;
		}
		if (pos>neg && importanza>=3) importanza += 0.3;
		if (pos<=neg && importanza>=3) importanza += 0.2;
		if (pos>neg && importanza<3) importanza -= 0.3;
		if (pos<=neg && importanza<3 ) importanza -= 0.5;
		self.interazioni.get(conoscente).importanza = importanza;
	}

	private void Bipolare_AggiornaImportanza(Giocatore conoscente, Giocatore self) {
		double importanza = self.interazioni.get(conoscente).importanza;
		double umore = self.umore;
		if (umore >=50) {
			double a = random.nextDouble();
			if (a<=1.0 && a>0.3) a/=3;
			importanza+=a;
		}
		else {
			double a = random.nextDouble()-1.0;
			if (a>=-1.0 && a<-0.3) a/=3;
			importanza-=a;
		}
		self.interazioni.get(conoscente).importanza = importanza;
	}

	private int Bipolare_input(int messaggio, Giocatore mittente, Giocatore self) {
		double umore = self.umore;
		int significato=0;
		if (self.vocabolario.containsKey(messaggio) == false) {
			significato = random.nextInt(11)-5;
			self.vocabolario.put(messaggio, significato);
			self.dizionario_out.get(significato).add(messaggio);
		}
		else significato = self.vocabolario.get(messaggio);

		if (umore >= 50) {
			if (messaggio%2==0) significato+=random.nextInt(3);
			else significato+=(random.nextInt(3)-3);
		}
		else {
			if (messaggio%2!=0) significato+=random.nextInt(3);
			else significato+=(random.nextInt(3)-3);
		}
		return significato;
	}

	private int Bipolare_output(Giocatore destinatario, Giocatore self) {
		double umore = self.umore;
		HashMap<Integer, LinkedList<Integer>> dizionario_out = self.dizionario_out;
		int significato=0;
		if (umore>=50) significato = random.nextInt(6);
		else significato = random.nextInt(6)-5;
		Collections.shuffle(dizionario_out.get(significato));
		return dizionario_out.get(significato).get(0);
	}

	private int Malvagio_input(int messaggio, Giocatore mittente, Giocatore self) {
		double umore = self.umore;
		int significato;
		if (self.vocabolario.containsKey(messaggio) == false) {
			significato = random.nextInt(11)-5;
			self.vocabolario.put(messaggio, significato);
			self.dizionario_out.get(significato).add(messaggio);
		}
		else significato = self.vocabolario.get(messaggio);

		if (umore<60) {
			if (significato<=0) significato+=3;
			else significato-=3;
		}
		else {
			if (significato<=0) significato+=1;
			else significato-=1;
		}

		return significato;
	}

	private int Malvagio_output(Giocatore destinatario, Giocatore self) {
		double umore = self.umore;
		LinkedList<Integer> storico = self.interazioni.get(destinatario).storico;
		HashMap<Integer, LinkedList<Integer>> dizionario_out = self.dizionario_out;	
		int significato=0;
		int pos=0;
		int neg=0;

		for (int i = 0; i < storico.size(); i++) {
			if (storico.get(i)>2) pos++;
			else if (storico.get(i)<-2) neg++;
			if(i==5) break;
		}
		if (umore >= 60) {
			if (neg>=pos) significato=random.nextInt(4)+2;
			else significato=random.nextInt(4)-5;
		}
		else {
			if (neg>=pos) significato=random.nextInt(3);
			else significato=random.nextInt(3)-3;
		}

		Collections.shuffle(dizionario_out.get(significato));
		return dizionario_out.get(significato).get(0);
	}

	private void Malvagio_AggiornaImportanza(Giocatore conoscente, Giocatore self) {
		double importanza = self.interazioni.get(conoscente).importanza;
		LinkedList<Integer> storico = self.interazioni.get(conoscente).storico;
		int pos=0;
		int neg=0;

		if (importanza>=1 && importanza<=3) {
			for (int i = 0; i < storico.size(); i++) {
				if (storico.get(i)>2) pos++;
				else if (storico.get(i)<-2) neg++;
				if(i==5) break;
			}

			if (neg>=pos) importanza+=0.3;
			else importanza-=0.3;
		}
		else if (importanza>3) importanza=3;

		self.interazioni.get(conoscente).importanza = importanza;	
	}

	private int Rancoroso_input(int messaggio, Giocatore mittente, Giocatore self) {
		int significato;
		if (self.vocabolario.containsKey(messaggio) == false) {
			significato = random.nextInt(11)-5;
			self.vocabolario.put(messaggio, significato);
			self.dizionario_out.get(significato).add(messaggio);
		}
		else significato = self.vocabolario.get(messaggio);
		return significato;
	}

	private int Rancoroso_output(Giocatore destinatario, Giocatore self) {
		LinkedList<Integer> storico = self.interazioni.get(destinatario).storico;
		HashMap<Integer, LinkedList<Integer>> dizionario_out = self.dizionario_out;
		int significato = 0;
		int contatore=0;
		int iterazioni=0;

		for (int elemento:storico ) {
			contatore += elemento;
			iterazioni++;
			if (iterazioni == 10) break;
		}
		if (storico.size()>0)significato = (contatore) / iterazioni; //questo if perche poi dava errore che non si puo dividere per 0
		else significato=0;

		if (significato>5) significato=5;
		if (significato<-5) significato=-5;

		Collections.shuffle(dizionario_out.get(significato));
		return dizionario_out.get(significato).get(0);
	}

	private void Rancoroso_AggiornaImportanza(Giocatore conoscente, Giocatore self) {
		LinkedList<Integer> storico = self.interazioni.get(conoscente).storico;
		double importanza = self.interazioni.get(conoscente).importanza;
		int pos = 0;
		int neg = 0;
		for (int i = 0; i < storico.size(); i++) {
			if (storico.get(i) > 2) pos++;
			else if (storico.get(i) < -2) neg++;
			if (i == 10) break;
		}
		if (neg >= pos) importanza -= 0.5;
		else importanza += 0.3;
		self.interazioni.get(conoscente).importanza = importanza;
	}

    private int Visionario_input(int messaggio, Giocatore mittente, Giocatore self) {
    	int significato=0;
    	if (self.vocabolario.containsKey(messaggio) == false) {
			significato = random.nextInt(11)-5;
			self.vocabolario.put(messaggio, significato);
			self.dizionario_out.get(significato).add(messaggio);
		}
    	else significato = self.vocabolario.get(messaggio); 
    	significato = random.nextInt(3)-1;
    	return significato;
    }		
	
    private int Visionario_output(Giocatore destinatario, Giocatore self) {
    	HashMap<Integer, LinkedList<Integer>> dizionario_out = self.dizionario_out;	
    	int significato=0;
     	double umore_M = destinatario.umore;
    	if (self.umore<=30) {
    		if (umore_M<=20) significato= random.nextInt(1)-3;
    		else significato = random.nextInt(1)-5;
    	}
    	else {
    		if (umore_M<=20) significato=  random.nextInt(1)+4;
    		else significato = random.nextInt(1)+3;
    	}
    	Collections.shuffle(dizionario_out.get(significato));
    	return dizionario_out.get(significato).get(0);
    }

    private void Visionario_AggiornaImportanza(Giocatore conoscente, Giocatore self) { // Da tenere d'occhio
        LinkedList<Integer> storico = self.interazioni.get(conoscente).storico;
        double importanza = self.interazioni.get(conoscente).importanza;
        int val = 0;
        for (int i = 0; i < storico.size(); i++) {
            if (storico.get(i) >= 2) val++;
            else val--;
            if (i == 5) break;
        }
        if (val > 3) importanza=0.3;
        else importanza =-0.5;
        self.interazioni.get(conoscente).importanza = importanza;
	}
	
	private int Player_input(int messaggio, Giocatore mittente, Giocatore self) {

		int significato;
		LinkedList<Integer> storicolimitato = new LinkedList<Integer>();
		if (self.vocabolario.containsKey(messaggio) == false) {
			significato = random.nextInt(11)-5;
			self.vocabolario.put(messaggio, significato);
			self.dizionario_out.get(significato).add(messaggio);
		}
		else significato = self.vocabolario.get(messaggio);

		System.out.println(mittente.nome + " " + mittente.cognome + " ti ha mandato questo messaggio --> " + messaggio + " (Tradotto = " + significato + ")");
		if (self.interazioni.get(mittente).storico.size()==0) {
			System.out.println("\nFino ad ora non hai mai scambiato messaggi con lui ");
		}
		else if (self.interazioni.get(mittente).storico.size()<=10) {
			System.out.println("\nQuesti sono gli ultimi messaggi che ti ha inviato --> " + self.interazioni.get(mittente).storico);
		}
		else {
			for (int i = 0; i < 10; i++) {
				storicolimitato.add(self.interazioni.get(mittente).storico.get(i));
			}
			System.out.println("\nQuesti sono gli ultimi messaggi che ti ha inviato --> " + storicolimitato);
		}

		continua=true;
		do {
			try {
				System.out.print("Come lo vuoi interpretare? (-7<=interpretazione<=7) \n");
				significato = inputScanner.nextInt();
				if (significato>7 || significato<-7) throw new InputMismatchException();
				continua=false;
			} catch (InputMismatchException e) {
				System.out.print("Errore! ");
			}
			inputScanner.nextLine(); 
		} while (continua);

		return significato;
	}

	private int Player_output(Giocatore destinatario, Giocatore self) {

		HashMap<Integer, LinkedList<Integer>> dizionario_out = self.dizionario_out;
		int significato=0;	    
		LinkedList<Integer> storicolimitato = new LinkedList<Integer>();
		System.out.println("\nIl tuo umore e' al " + Round(self.umore) + "%");
		System.out.println("Hai appena incontrato " + destinatario.nome + " " + destinatario.cognome + ", per te questa persona ha importanza: " + self.interazioni.get(destinatario).importanza);
		if (self.interazioni.get(destinatario).storico.size()==0) {
			System.out.println("\nFino ad ora non hai mai scambiato messaggi con lui ");
		}
		else if (self.interazioni.get(destinatario).storico.size()<=10) {
			System.out.println("\nQuesti sono gli ultimi messaggi che ti ha inviato --> " + self.interazioni.get(destinatario).storico);
		}
		else {
			for (int i = 0; i < 10; i++) {
				storicolimitato.add(self.interazioni.get(destinatario).storico.get(i));
			}
			System.out.println("\nQuesti sono gli ultimi messaggi che ti ha inviato --> " + storicolimitato);
		}

		continua=true;
		do {
			try {
				System.out.print("Che messaggio vuoi mandare? (-5<=messaggio<=5) \n");
				significato = inputScanner.nextInt();
				if (significato>5 || significato<-5) throw new InputMismatchException();
				continua=false;
			} catch (InputMismatchException e) {
				System.out.print("Errore! ");
			}
			inputScanner.nextLine(); 
		} while (continua);


		Collections.shuffle(dizionario_out.get(significato));
		return dizionario_out.get(significato).get(0);
	}

	private void Player_AggiornaImportanza(Giocatore conoscente, Giocatore self) {
		LinkedList<Integer> storicolimitato = new LinkedList<Integer>();
		System.out.print("Questi sono stati gli ultimi messaggi che ti ha mandato " + conoscente.nome + " " + conoscente.cognome);

		if (self.interazioni.get(conoscente).storico.size()<=10) {
			System.out.print(" --> " + self.interazioni.get(conoscente).storico);
		}
		else {
			for (int i = 0; i < 10; i++) {
				storicolimitato.add(self.interazioni.get(conoscente).storico.get(i));
			}
			System.out.print(" --> " + storicolimitato);
		}

		double incremento=0;
		continua=true;
		do {
			try {
				System.out.println("\nDi quanto vuoi incrementare-decrementare la sua importanza per te? (-0,5<valore<0,5)" );
				incremento = inputScanner.nextDouble();
				if (incremento>0.5 || incremento<-0.5) throw new InputMismatchException();
				continua=false;
			} catch (InputMismatchException e) {
				System.out.print("Errore! ");
			}
			inputScanner.nextLine(); 
		} while (continua);

		self.interazioni.get(conoscente).importanza += incremento;
		if (self.interazioni.get(conoscente).importanza>5) self.interazioni.get(conoscente).importanza=5;
	}
}