import { Component, OnInit } from '@angular/core';
import { MenuController } from '@ionic/angular';
import { Platform } from '@ionic/angular';
import { SplashScreen } from '@ionic-native/splash-screen/ngx';
import { StatusBar } from '@ionic-native/status-bar/ngx';
import { AngularFirestore } from 'angularfire2/firestore';
import { AuthenticationService } from './shared/authentication-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss']
})
export class AppComponent implements OnInit {
  navigate : any;
  mostrar: boolean = false;
  constructor(private platform    : Platform,
              private splashScreen: SplashScreen,
              private statusBar   : StatusBar,
              public authService: AuthenticationService,
              private router:Router)
  {

    this.initializeApp();
  }

  initializeApp() {
    this.platform.ready().then(() => {
      this.statusBar.styleDefault();
      this.splashScreen.hide();
      if(this.authService.isLoggedIn){
        this.mostrar= true;
      }else{
        this.mostrar= true;
        this.router.navigate(['/Login']);
      }
    });
  }

  ClickTest(){

    alert('Click');

  }

  ngOnInit() {

  }
  signOut(){
    this.authService.SignOut().then(()=>{
        window.location.reload();

    });

  }
}
