package com.example.dk88;

import static com.example.dk88.Student.STATUS_BAN_USER;
import static com.example.dk88.Student.STATUS_NEW_USER;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    Button btnSignin,btnSignup;
    EditText edtUser, edtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);
        btnSignin = (Button) findViewById(R.id.signin1);
        edtUser = (EditText) findViewById(R.id.Username);
        edtPass = (EditText) findViewById(R.id.Password);
        btnSignup=(Button) findViewById(R.id.signup);
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> loginInfo = new HashMap<>();
                loginInfo.put("userName", edtUser.getText().toString());
                loginInfo.put("hashPass", edtPass.getText().toString());
                Call<ResponseObject> call = ApiUserRequester.getJsonPlaceHolderApi().login(loginInfo);
                call.enqueue(new Callback<ResponseObject>() {
                    @Override
                    public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(SignInActivity.this, "Error", Toast.LENGTH_LONG).show();
                            return;
                        }
                        ResponseObject tmp = response.body();
                        String token = response.headers().get("token");

                        if (tmp.getRespCode() != ResponseObject.RESPONSE_OK) {
                            Toast.makeText(SignInActivity.this, tmp.getMessage(), Toast.LENGTH_LONG).show();
                            return;
                        }
                        Map<String, Object> data = (Map<String, Object>) tmp.getData();

                        Integer userRole = Math.toIntExact(Math.round(Double.parseDouble(data.get("roleCode").toString())));
                        if(userRole.equals(User.ROLE_CODE_ADMIN)){
                            Admin admin=new Admin();
                            admin.setUserName(data.get("userName").toString());
                            admin.setName(data.get("name").toString());
                            admin.setEmail(data.get("email").toString());
                            admin.setPhoneNumber(data.get("phoneNumber").toString());
                            admin.setRoleCode(userRole);

                            Intent intent = new Intent(SignInActivity.this, AdminProfileActivity.class);
                            intent.putExtra("token",token);
                            intent.putExtra("admin",admin);
                            startActivity(intent);

                        }
                        else{
                            Student student=new Student();
                            student.setRoleCode(userRole);
                            student.setUserName(data.get("userName").toString());
                            student.setName(data.get("name").toString());
                            student.setPhoneNumber(data.get("phoneNumber").toString());
                            student.setStatus(Math.toIntExact(Math.round(Double.parseDouble(data.get("status").toString()))));
                            student.setStudentID(data.get("studentID").toString());

                            if(student.getStatus()==STATUS_NEW_USER){
                                Intent intent = new Intent(SignInActivity.this, StudentIdentity.class);
                                intent.putExtra("token",token);
                                intent.putExtra("student",student);
                                startActivity(intent);
                            }
                            else if(student.getStatus()==STATUS_BAN_USER){
                                Intent intent=new Intent(SignInActivity.this,StudentBanStatus.class);
                                startActivity(intent);
                            }
                            else{
                                Intent intent = new Intent(SignInActivity.this, ProfileActivity.class);
                                intent.putExtra("token",token);
                                intent.putExtra("student",student);
                                startActivity(intent);
                            }

                        }
                        Toast.makeText(SignInActivity.this, "Login success as " + data.get("name"), Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(Call<ResponseObject> call, Throwable t) {
                        Toast.makeText(SignInActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                });
            }

        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
