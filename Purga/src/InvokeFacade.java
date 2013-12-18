/*
Deprecated Class from Oracle

Edited by:
    DATE:           USER:               ChangeLog
    Original        Oracle              Original Class
    28/11/2013      PT-SI\XPTA491       Added ReadCVS class and introduced batch alterations.
*/

import javax.naming.Context;

import oracle.soa.management.CompositeDN;
import oracle.soa.management.facade.Locator;
import oracle.soa.management.facade.LocatorFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class InvokeFacade {
    public static void main(String[] args) {
        int success=0;
        int failure=0;

        try {
            // Read and convert CSV containing instance ID's and Composite names
            ReadCVS obj = new ReadCVS();
            Map<String, String> table = obj.run();

            System.out.println();
            System.out.println("------------- START ALTERATION? -------------");
            System.out.println("ready to proceed? (y/n)");

            try{
                BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                String s = bufferRead.readLine();
                if (s.equals("y") || s.equals("Y")) {
                    // Prepare Environment
                    System.out.println("Preparing Environment...");
                    Hashtable jndiProps = new Hashtable();
                    jndiProps.put(Context.PROVIDER_URL,
                            "t3://vkdmtc07.telecom.pt:8001/soa-infra");
                    jndiProps.put(Context.INITIAL_CONTEXT_FACTORY,
                            "weblogic.jndi.WLInitialContextFactory");
                    jndiProps.put(Context.SECURITY_PRINCIPAL, "weblogic");
                    jndiProps.put(Context.SECURITY_CREDENTIALS, "mtc2012!");
                    jndiProps.put("dedicated.connection", "true");
                    Locator locator = LocatorFactory.createLocator(jndiProps);
                    System.out.println("Environment Prepared...");
                    System.out.println("---------------------------------------------");
                    System.out.println("------------ STARTING ALTERATION ------------");
                    for (Map.Entry<String, String> entry : table.entrySet()) {
                        String id=entry.getKey();
                        String composite= entry.getValue();
                        try {
                            locator.executeCompositeMethod(new CompositeDN(composite),
                                    "abortCompositeInstance",
                                    new Object[]{id});
                            System.out.println("State Alteration SUCCESS !\t--- Instance id=" + id);
                            success++;
                        } catch (Exception e){
                            System.out.println("State Alteration FAILED !\t--- Instance id=" + id);
                            failure++;
                        }
                    }
                    System.out.println("Successful State Alterations: " + success);
                    System.out.println("Failed State Alterations: " + failure);
                }
                System.out.println("Program Complete");
            }catch(IOException e){
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
