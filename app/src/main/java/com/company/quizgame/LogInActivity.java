package com.company.quizgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    EditText email, password;
    ProgressBar progressBar;
    Button signIn;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getSupportActionBar().setTitle("Log In");

        email = findViewById(R.id.emailSignInEt);
        password = findViewById(R.id.passwordSignInEt);
        progressBar = findViewById(R.id.progressBarSignIn);
        progressBar.setVisibility(View.INVISIBLE);

        signIn = findViewById(R.id.signInBtn);
        signIn.setOnClickListener(this);

        findViewById(R.id.googleSignInBtn).setOnClickListener(this);
        findViewById(R.id.signUpTv).setOnClickListener(this);
        findViewById(R.id.forgotTv).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signInBtn) {
            String userEmail = email.getText().toString();
            String userPassword = password.getText().toString();
            signInWithTheFirebase(userEmail, userPassword);
        } else if (v.getId() == R.id.googleSignInBtn) {
            signInGoogle();
        } else if (v.getId() == R.id.signUpTv) {
            Intent i = new Intent(LogInActivity.this, SignUpActivity.class);
            startActivity(i);
        } else if (v.getId() == R.id.forgotTv) {
            Intent i = new Intent(LogInActivity.this, ForgotActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void signInWithTheFirebase(String userEmail, String userPassword){
        progressBar.setVisibility(View.VISIBLE);
        signIn.setClickable(false);
        auth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent i = new Intent(LogInActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(LogInActivity.this, "You have Signed in successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LogInActivity.this, "Sorry something is wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null){
            Intent i = new Intent(LogInActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void signInGoogle(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        signIn();
    }

    public void signIn(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            firebaseSignInWithGoogle(task);
        }
    }

    private void firebaseSignInWithGoogle(Task<GoogleSignInAccount> task){
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Toast.makeText(LogInActivity.this, "Signed In Successfully", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(LogInActivity.this, MainActivity.class);
            startActivity(i);
            finish();
            firebaseGoogleAccount(account);
        } catch (ApiException e) {
            e.printStackTrace();
            Toast.makeText(this, "Sorry something is wrong!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseGoogleAccount(GoogleSignInAccount account){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = auth.getCurrentUser();
                } else {

                }
            }
        });
    }
}