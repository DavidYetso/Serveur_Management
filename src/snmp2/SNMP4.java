package snmp2;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.Vector;

public class SNMP4 {
    private TransportMapping transport;
    private CommunityTarget target;
    private PDU pdu;
    private Snmp snmp;
    private String comunity;
    private String mib;
    private int retries;
    private int timeout;
    private boolean asynchrone;
    private boolean connect;


    public SNMP4(String comunity, int retries, int timeout, String ip) {
        this.comunity = comunity;
        this.retries = retries;
        this.timeout = timeout;
        try {
            transport = new DefaultUdpTransportMapping();
            transport.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
        target = new CommunityTarget();
        target.setVersion(SnmpConstants.version1);
        target.setCommunity(new OctetString(this.comunity));
//        Address targetAddress = new UdpAddress("192.168.1.3/161");
         Address targetAddress = GenericAddress.parse("udp:" + ip + "/161");
        target.setAddress(targetAddress);
        target.setRetries(this.retries);
        target.setTimeout(this.timeout);
        pdu = new PDU();
        snmp = new Snmp(transport);

    }
    public String getMib() {
        return this.mib;
    }
    public void setMib(String mib) {
        this.mib = mib;
    }
    public void setAsynchrone(boolean asynchrone) {
        this.asynchrone = asynchrone;
    }
    public boolean isConnect() {
        return connect;
    }
    public void setConnect(boolean connect) {
        this.connect = connect;
    }

    public void get() {
        if (isConnect()) {
            pdu.setType(PDU.GET);
            pdu.add(new VariableBinding(new OID(this.mib)));
            if (asynchrone) {
                asynchrone();
            } else {
                ResponseEvent paquetReponse = null;
                try {
                    paquetReponse = snmp.get(pdu, target);
                } catch (IOException ex) {
                    System.err.println("erreur IO " + ex);
                }

                System.out.println("Requete SNMP envoyée à l'agent");
                afficherReponse(paquetReponse);
            }
        }
    }
    public void set(String valeur) {
        if (isConnect()) {
            pdu.setType(PDU.SET);
            pdu.add(new VariableBinding(new OID(mib), new OctetString(valeur)));
            if (asynchrone) {
                asynchrone();
            } else {
                ResponseEvent paquetReponse = null;
                try {
                    paquetReponse = snmp.set(pdu, target);
                } catch (IOException ex) {
                    System.err.println("erreur IO " + ex);
                }

                System.out.println("Requete SNMP envoyée à l'agent");
                afficherReponse(paquetReponse);
            }
        }
    }
    public void getNext() {
        if (isConnect()) {
            pdu.setType(PDU.GETNEXT);
            pdu.add(new VariableBinding(new OID(this.mib)));
            ResponseEvent paquetReponse = null;

            if (asynchrone) {
                asynchrone();
            } else {
                try {
                    paquetReponse = snmp.getNext(pdu, target);
                } catch (IOException ex) {
                    System.err.println("erreur IO " + ex);
                }

                System.out.println("Requete SNMP envoyée à l'agent");
                afficherReponse(paquetReponse);
                PDU rep = paquetReponse.getResponse();
                VariableBinding vb = rep.get(rep.size() - 1);
                setMib(vb.getOid().toString());
            }
        }
    }
    private void asynchrone() {
        SnmpListener listener = new SnmpListener(snmp);
        try {
            snmp.send(pdu, target, null, listener);
            synchronized (snmp) {
                snmp.wait();
            }
        } catch (IOException | InterruptedException e) {e.printStackTrace();}
    }

    private void afficherReponse(ResponseEvent paquetReponse) {
        if (paquetReponse != null) {
            PDU pduReponse = paquetReponse.getResponse();
            System.out.println("erreur = " + pduReponse.getErrorStatus());
            System.out.println("réponse = " + pduReponse.getErrorStatusText());
            Vector vecReponse = pduReponse.getVariableBindings();
            for (int i = 0; i < vecReponse.size(); i++)
                System.out.println("Elément n°" + i + " : " + vecReponse.elementAt(i));
        }
        else System.err.println("Paquet reponse = null");
    }

}
