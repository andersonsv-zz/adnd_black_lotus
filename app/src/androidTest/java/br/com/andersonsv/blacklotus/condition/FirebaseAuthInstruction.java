package br.com.andersonsv.blacklotus.condition;

import com.azimolabs.conditionwatcher.Instruction;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthInstruction extends Instruction {
    @Override
    public String getDescription() {
        return "Login in firebase auth";
    }

    @Override
    public boolean checkCondition() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return auth.getCurrentUser() != null && auth.getCurrentUser().getUid() != null;
    }
}
