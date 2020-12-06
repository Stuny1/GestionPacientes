import { Component,OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { AuthenticationService } from "../shared/authentication-service";
import { AppComponent } from "../app.component";

@Component({
  selector: 'app-login',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
})

export class LoginPage implements OnInit {
  credentialData:any;
  constructor(
    public authService: AuthenticationService,
    public router: Router,
    private appcomponent:AppComponent) {
      if(this.authService.isLoggedIn){
        this.appcomponent.mostrar = true;
        this.router.navigate(['/Inicio']);
      }else{
        this.appcomponent.mostrar = false;
      }
    }

  ngOnInit() {

  }

  logIn(email, password) {
    this.authService.SignIn(email.value, password.value).then((res) => {
              this.authService.SetUserData(res.user);
              this.authService.credentialData = res.credential;
              this.appcomponent.mostrar = true;
              this.router.navigate(['/Inicio']);


          }).catch((error) => {
            window.alert(error.message)
          })
  }

}
