import java.util.LinkedList;

public class PatientScheduler {

    private static class Node {
        int priority;
        LinkedList<Patient> patients;
        int height;
        Node left, right;

        Node(int priority, Patient patient) {
            this.priority = priority;
            this.patients = new LinkedList<>();
            this.patients.add(patient);
            this.height = 1;
        }
    }


    private Node root;
    private int totalPatients;

    public PatientScheduler() {
        this.root = null;
        this.totalPatients = 0;
    }

    private int nodeHeight(Node node) {
        return (node == null) ? 0 : node.height;
    }

    private void newHeight(Node node) {
        if (node != null) {
            node.height = 1 + Math.max(nodeHeight(node.left), nodeHeight(node.right));
        }
    }

    private int balanceKeeper(Node node) {
        return (node == null) ? 0 : nodeHeight(node.left) - nodeHeight(node.right);
    }
    public void addPatient(int priority, Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }
        root = insert(root, priority, patient);
        totalPatients++;
        System.out.println("Added: " + patient.getFullName() + " (Priority: " + priority + ")");
    }

    public boolean removePatient(int priority) {

        Node current = root;

        while (current != null) {
            if (priority < current.priority) {
                current = current.left;
            } else if (priority > current.priority) {
                current = current.right;
            } else {
                if (!current.patients.isEmpty()) {
                    Patient removed = current.patients.removeFirst();
                    totalPatients--;
                    System.out.println("Removed: " + removed.getFullName() + " (Priority: " + priority + ")");
                }

                if (current.patients.isEmpty()) {
                    root = delete(root, priority);
                }
                return true;
            }
        }
        System.out.println("No patients found with priority: " + priority);
        return false;
    }

    public boolean removePatientById(int id) {
        return removePatientsById(root, id);
    }

    private boolean removePatientsById(Node node, int id) {
        if (node == null) {
            return false;
        }

        // Check patients in current node
        for (int i = 0; i < node.patients.size(); i++) {
            if (node.patients.get(i).getId() == id) {
                node.patients.remove(i);
                totalPatients--;

                if (node.patients.isEmpty()) {
                    root = delete(root, node.priority);
                }
                return true;
            }
        }

        // Search in left and right children
        return removePatientsById(node.left, id) || removePatientsById(node.right, id);
    }
     public Patient getHighestPriorityPatient() {
        if (root == null) {
            return null;
        }

        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.patients.getFirst();
     }

     public Patient getLowestPriorityPatient(){
        if (root == null) {
            return null;
        }

        Node current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.patients.getFirst();
     }

     public void printSchedule() {
         if (root ==  null) {
             System.out.println("No patients scheduled for today.");
             return;
         }
         System.out.println("\n    Patient Schedule   ");
         inOrderPrinter(root);
         System.out.println("Total patients: " + totalPatients);
         System.out.println("                      \n");
     }

     private void inOrderPrinter(Node node) {
        if (node == null) {
            return;
        }
        inOrderPrinter(node.left);
        System.out.println("Priority: " + node.priority + ": " + node.patients.size() + " patient(s)");
        for (Patient p : node.patients) {
            System.out.println(" -" + p);
        }
        inOrderPrinter(node.right);
     }

     public void printPriorityLevels(){
        if (root == null) {
            System.out.println("No priority levels available");
            return;
        }
        System.out.println("\n    Priority Levels    ");
        printPriorityLevels(root);
        System.out.println("                       \n");
     }

    private void printPriorityLevels(Node node){
        if (node == null) return;
        printPriorityLevels(node.left);
        System.out.println("Priority " + node.priority + ": " + node.patients.size() + " patient(s)");
        printPriorityLevels(node.right);
    }

    public int getPatientCount() {
        return totalPatients;
    }

    public boolean isEmpty() {
        return root == null;
    }
    private Node balance(Node node) {
        int balance = balanceKeeper(node);
        boolean leftHeavy = balance > 1;
        boolean rightHeavy = balance < -1;
        boolean imbalanceLR = leftHeavy && balanceKeeper(node.left) < 0;
        boolean imbalanceRL = rightHeavy && balanceKeeper(node.right) > 0;

        if (leftHeavy) {
            if (imbalanceLR) {
                node.left = leftRotation(node.left);
            }
            return rightRotation(node);
        }
        if (rightHeavy) {
            if (imbalanceRL) {
                node.right = rightRotation(node.right);
            }
            return leftRotation(node);
        }
        return node;
    }

     private Node insert(Node node, int priority, Patient patient) {
        if (node == null) {
            return new Node(priority, patient);
        }

        if (priority < node.priority) {
            node.left = insert(node.left, priority, patient);
        }
        else if (priority > node.priority) {
            node.right = insert(node.right, priority, patient);
        }
        else {
            node.patients.addLast(patient);
            return node;
        }

        newHeight(node);
        return balance(node);
     }

     private Node delete(Node node, int priority) {
        if (node == null) {
            return null;
        }

        if (priority < node.priority) {
            node.left = delete(node.left, priority);
        }
        else if (priority > node.priority) {
            node.right = delete(node.right, priority);
        }
        else {
            if (node.left == null || node.right == null) {
                return (node.left != null) ? node.left : node.right;
            }
            else {
                Node minNode = getSmallestNode(node.right);
                node.priority = minNode.priority;
                node.patients = minNode.patients;
                node.right = delete(node.right, minNode.priority);
            }
        }
        newHeight(node);
        return balance(node);
     }

     private Node getSmallestNode(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
     }

     private Node leftRotation(Node a) {
        Node b = a.right;
        Node T2 = b.left;

        b.left = a;
        a.right = T2;

        newHeight(a);
        newHeight(b);

        return b;
     }

    private Node rightRotation(Node b) {
        Node a = b.left;
        Node T2 = a.right;

        a.right = b;
        b.left = T2;

        newHeight(b);
        newHeight(a);

        return a;
    }

    public String callNextPatient(){
        if (root == null) {
            return "No patients";
        }

        Node current = root;
        while (current.right != null) {
            current = current.right;
        }

        Patient nextPatient = current.patients.removeFirst();
        String result = "Calling: " + nextPatient.getFullName() + " (Priority " + current.priority + " | ID: " + nextPatient.getId() + ")";

        if (current.patients.isEmpty()) {
            root = delete(root, current.priority);
        }

        totalPatients--;
        return result;
    }

    public Patient peekNextPatient() {
        if (root == null) return null;

        Node current = root;
        while (current.right != null){
            current = current.right;
        }
        return current.patients.getFirst();
    }
}


