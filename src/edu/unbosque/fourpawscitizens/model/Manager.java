package edu.unbosque.fourpawscitizens.model;

import edu.unbosque.fourpawscitizens.model.daos.Pet;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class Manager<t> {
    private final File f;
    private Scanner sc;
    private ArrayList<Pet> petList;
    private BufferedReader br;

    public Manager() {

        // 1 - Se definen los tres atributos más importantes del programa
        // - El archivo csv
        f = new File("pets-citizens.csv");

        // - El Scanner
        sc = new Scanner(System.in);

        // - Y el ArrayList de mascotas
        petList = new ArrayList<Pet>();

        // 2 - Aquí se ejecuta el bucle en donde el usuario puede elegir qué hacer dentro del programa
        // - Si no se ha cargado el archivo, el programa simplemente le advertirá al usuario que no hay
        //archivo cargado y continuará ofreciendo las 6 opciones. Esto se indica con el boolean canDoTheRest
        boolean canDoTheRest = false;
        while (1 == 1) {
            System.out.println("-----------------------------------------------" +
                    "\nIngrese un número para seleccionar una funcionalidad:" +
                    "\n1. Cargar los Datos del Archivo" +
                    "\n2. Asignar un ID a todas las mascotas." +
                    "\n3. Buscar una mascota por su número de microchip." +
                    "\n4. Contar cuántos animales de una especie hay." +
                    "\n5. Buscar animales peligrosos en una zona." +
                    "\n6. Buscar animales con los parámetros sexo, especie, tamaño y peligro potencial." +
                    "\n7. Salir del programa." +
                    "\n-----------------------------------------------");
            int s = sc.nextInt();
            switch (s) {
                case 1:
                    //CARGACIÓN
                    uploadData();
                    canDoTheRest = true;
                    break;
                case 2:
                    if(canDoTheRest == true) {
                        //ASIGNACIÓN
                        for (int i = 0; i < petList.size(); i++) {
                            assignID(i, 3);
                        }
                        System.out.println("El proceso de asignación de ids ha finalizado");
                    } else { System.out.println("No se ha cargado el archivo"); break; }
                    break;
                case 3:
                    if(canDoTheRest == true) {
                        //CHIP-BUSCACIÓN
                        System.out.println("Ingrese el chip de la mascota");
                        try {
                            long pLong = sc.nextLong();
                            findByMicrochip(pLong);
                        } catch (Exception e) {
                            System.out.println("Dato inválido. Cerrando programa");
                            e.printStackTrace();
                            System.exit(1);
                        }
                    } else { System.out.println("No se ha cargado el archivo"); break; }
                    break;
                case 4:
                    sc.nextLine();
                    if(canDoTheRest == true) {
                        //SPECIES-CONTACIÓN
                        String speciesSearch = sc.nextLine();
                        int countResult = countBySpecies(speciesSearch);
                        if (countResult == 0) {
                            System.out.println("No se encontraron especímenes de la especie especificada");
                        } else {
                            System.out.println("El número de animales de la especie " + speciesSearch + " es: " + countResult);
                        }
                    } else { System.out.println("No se ha cargado el archivo"); break; }
                    break;

                case 5:
                    sc.nextLine();
                    if(canDoTheRest == true) {
                        //PD-NEIGHBORHOOD-BUSCACIÓN
                        int pN = 0;
                        try{ pN = sc.nextInt(); } catch (Exception e) { System.out.println("Cantidad ingresada inválida. Cerrando programa"); System.exit(1); }
                        String tL = "";
                        tL = sc.nextLine();
                        String nH = "";
                        nH = sc.nextLine();
                        findBypotentDangerousInNeighborhood(pN, tL, nH);
                    } else { System.out.println("No se ha cargado el archivo"); break; }
                    break;

                case 6:
                    sc.nextLine();
                    if(canDoTheRest == true) {
                        //MULTIPLEFIELDS-BUSCACIÓN
                        String pSpecies = "";
                        pSpecies = sc.nextLine();
                        String pSex = "";
                        pSex = sc.nextLine();
                        String pSize = "";
                        pSize = sc.nextLine();
                        String pPotentDangerous = "";
                        pPotentDangerous = sc.nextLine();
                        findByMultipleFields(pSpecies, pSex, pSize, pPotentDangerous);
                    } else { System.out.println("No se ha cargado el archivo"); break; }
                    break;

                case 7:
                    System.exit(0);
                    break;

            }
        }

    }

    public void uploadData() {

        try {
            //se carga el archivo en el reader
            br = new BufferedReader(new FileReader(f));
        } catch (Exception e) {
            System.exit(1);
        }

        //se omite la primera línea
        try {
            br.readLine();
        } catch (Exception e) {
            System.exit(1);
        }

        //Inicialización de los atributos
        String id = "NO-ID";
        long mc = 0;
        String sp = "";
        String sex = "";
        String size = "";
        boolean pd = false;
        String nh = "";

        //Ciclo de carga en el arrayTemp. Se separan todos los datos de cada mascota como Strings.
        ArrayList<String[]> arrayTemp = new ArrayList<String[]>();
        for (int i = 0; i < 23410; i++) {
            try {
                arrayTemp.add(br.readLine().split(";"));
            } catch (Exception e) {
            }
        }

        //Ciclo de carga en el array PetList
        for (int i = 0; i < arrayTemp.size(); i++) {
            boolean skippable = false;
            //Esta parte guarda los datos de un animal
            //MICROCHIP
            try {
                mc = Long.parseLong(arrayTemp.get(i)[0]);
            } catch (NumberFormatException e) { skippable = true; }
            //SPECIES
            sp = arrayTemp.get(i)[1];
            //SEX
            sex = arrayTemp.get(i)[2];
            //SIZE
            size = arrayTemp.get(i)[3];
            //POTENTIAL DANGER
            String next = arrayTemp.get(i)[4];
            if (next.equals("SI")) {
                pd = true;
            } else {
                pd = false;
            }
            //NEIGHBORHOOD
            try {
                nh = arrayTemp.get(i)[5];
            } catch (Exception e) { skippable = true; }

            //Ahora se crea el objeto Pet
            Pet newPet = new Pet(id, mc, sp, sex, size, pd, nh);
            if(skippable == false){ petList.add(newPet); } else {}

        }
        System.out.println("El proceso de carga del archivo ha finalizado");
    }

    public void assignID(int index, int enCuenta) {
        //System.out.println("asignando id a "+index);
        //ASIGNACIÓN
        String newID = "";

        //String where the ID sequence is built
        newID = petList.get(index).getMicrochip() + "";
        newID = newID.substring(newID.length() - (enCuenta + 1), newID.length() - 1);

        //Species
        if (petList.get(index).getSpecies().equals("CANINO")) {
            newID += "-C";
        } else {
            newID += "-G";
        }

        //Sex ( ͡° ͜ʖ ͡°)
        if (petList.get(index).getSex().equals("HEMBRA")) {
            newID += "H";
        } else {
            newID += "M";
        }

        //Size
        if (petList.get(index).getSize().equals("MINIATURA")) {
            newID += "MI";
        } else if (petList.get(index).getSize().equals("PEQUEÑO")) {
            newID += "P";
        } else if (petList.get(index).getSize().equals("MEDIANO")) {
            newID += "M";
        } else {
            newID += "G";
        }

        //Dangerous
        if (petList.get(index).isPotentDangerous() == true) {
            newID += "T";
        } else {
            newID += "F";
        }

        //Neighborhood
        newID += "-" + petList.get(index).getNeighborhood();

        petList.get(index).setId(newID);


        //DESREPETICIÓN
        for (int i = index - 1; 0 <= i; i--) {
            if (petList.get(index).getId().equals(petList.get(i).getId())) {
                //System.out.println(petList.get(index).getId()+" es igual a "+petList.get(i).getId());
                int pIndex = index;
                int pEnCuenta = enCuenta + 1;
                assignID(pIndex, pEnCuenta);
            }
        }

    }

    public void findByMicrochip(long microchip) {
        for (int i = 0; i < petList.size(); i++) {
            if (petList.get(i).getMicrochip() == microchip) {
                System.out.println("ID: " + petList.get(i).getId());
                System.out.println("Species: " + petList.get(i).getSpecies());
                System.out.println("Gender: " + petList.get(i).getSex());
                System.out.println("Size: " + petList.get(i).getSize());
                System.out.println("Potentially Dangerous: " + petList.get(i).isPotentDangerous());
                System.out.println("Neighborhood: " + petList.get(i).getNeighborhood());
                break;
            }
        }
    }

    public int countBySpecies(String species) {
        if(species.equalsIgnoreCase("felino")){
            species="FELINO";
        }
        if(species.equalsIgnoreCase("canino")){
            species="CANINO";
        }
        int count = 0;
        for (int i = 0; i < petList.size(); i++) {
            if (species.equals(petList.get(i).getSpecies())) {
                count++;
            }
        }
        return count;
    }

    public int findBypotentDangerousInNeighborhood(int n, String position, String neighborhood) {
        int count = 0;
        if (position.equalsIgnoreCase("TOP")) {
            for (int i = 0; i<petList.size(); i++) {
                if (petList.get(i).getNeighborhood().equalsIgnoreCase(neighborhood) && petList.get(i).isPotentDangerous() == true) {
                    System.out.println("ID: " + petList.get(i).getId());
                    System.out.println("Species: " + petList.get(i).getSpecies());
                    System.out.println("Gender: " + petList.get(i).getSex());
                    System.out.println("Size: " + petList.get(i).getSize());
                    System.out.println("Potentially Dangerous: " + petList.get(i).isPotentDangerous());
                    System.out.println("Neighborhood: " + petList.get(i).getNeighborhood());
                    count++;
                    if (count == n){ break; }
                }
            }
        } else if (position.equalsIgnoreCase("LAST")) {
            for (int i = petList.size()-1; 0>=i; i--) {
                if (petList.get(i).getNeighborhood().equalsIgnoreCase(neighborhood) && petList.get(i).isPotentDangerous() == true) {
                    System.out.println("ID: " + petList.get(i).getId());
                    System.out.println("Species: " + petList.get(i).getSpecies());
                    System.out.println("Gender: " + petList.get(i).getSex());
                    System.out.println("Size: " + petList.get(i).getSize());
                    System.out.println("Potentially Dangerous: " + petList.get(i).isPotentDangerous());
                    System.out.println("Neighborhood: " + petList.get(i).getNeighborhood());
                    count++;
                    if (count == n){ break; }
                }
            }
        }
        return count;
    }

    public void findByMultipleFields(String species, String sex, String size, String potentDangerous){
        Boolean ppd = false;
        if (potentDangerous.equals("SI")){ ppd = true; } else if (potentDangerous.equals("NO")){ ppd = false; }
        else {
            System.out.println("Dato inválido. Cerrando programa"); System.exit(1);
        }

        for (int i = 0; i<petList.size(); i++) {
            if(petList.get(i).getSex().equals(sex) && petList.get(i).getSpecies().equals(species)
                    && petList.get(i).getSize().equals(size) && petList.get(i).isPotentDangerous() == ppd){
                System.out.println("ID: " + petList.get(i).getId());
            }
        }
    }

}
