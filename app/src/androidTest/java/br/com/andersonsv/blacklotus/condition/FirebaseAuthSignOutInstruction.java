package br.com.andersonsv.blacklotus.condition;

import com.azimolabs.conditionwatcher.Instruction;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthSignOutInstruction extends Instruction {
    @Override
    public String getDescription() {
        return "Sign out in firebase auth";
    }

    @Override
    public boolean checkCondition() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return auth != null && auth.getCurrentUser() == null;
    }
}
