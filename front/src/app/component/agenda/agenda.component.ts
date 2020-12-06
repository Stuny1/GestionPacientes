import { Component, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import { CommonModule, formatDate } from "@angular/common";
import { ViewEncapsulation } from '@angular/core';
import { FirebaseService } from '../../services/firebase.service';
import { AlertController } from '@ionic/angular';
import { DomSanitizer } from '@angular/platform-browser';
import { AgendaServiceService } from '../../services/agenda-service.service';
import { AuthenticationService } from "../../shared/authentication-service";


interface PacienteData {
  id:number;
  Nombre: string;
  Apellido: string;
  Edad: number;
  Profilepic: string;
}

interface SesionData{
  paciente: string;
  doctor: string;
  fecha: Date;
}
interface DoctorData {
  id:number;
  Nombre: string;
  Profilepic: string;
}
@Component({
  selector: 'app-agenda',
  templateUrl: './agenda.component.html',
  styleUrls: ['./agenda.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class AgendaComponent implements OnInit {
  officeid = null;
  currentSelected = null;
  currentSelected2 = null;
  doctorseleccionado = null;
  pacienteseleccionado = null;
  customPickerOptions: any;
  horaSet:any;
  DoctorValidado:boolean=false;
  PacienteValidado:boolean=false;
  FechaValidado:boolean=false;
  PacienteList = null;
  pacienteData: PacienteData;
  DoctorList = null;
  doctorData: DoctorData;
  sesionData: SesionData;
  SesionList = [];
  ModifySesion: any;
  ModifyUser: any;
  ModifyChiro: any;
  private sub;
  public param;
  fecha: Date = null;
  public BuscarHoraEstado=0;
  public meses = ['Ene','Feb','Mar','Abr','May','Jun','Jul','Ago','Sep','Oct','Nov','Dic'];
  public dias = ['Dom','Lun','Mar','Mier','Jue','Vie','Sab'];
  public columns: any;
  public rows: any;
  public Estados = ["REQUESTED", "PENDING", "RESCHEDULED", "IN_WAITING_ROOM", "IN_PROGRESS", "FINALIZED", "CANCELLED"]


  constructor(
    public domSanitizer: DomSanitizer,
    private router:Router,
    private firebaseService: FirebaseService,
    public alertController: AlertController,
    private agendaService: AgendaServiceService,
    public authService: AuthenticationService

  ) {
    this.pacienteData = {} as PacienteData;
    this.doctorData = {} as DoctorData;
    this.sesionData = {} as SesionData;
    /*this.customPickerOptions = {
      buttons: [{
        text: 'Aceptar',
        handler: (res) => {
          console.log('Save Year', res)
          this.mydt.value = res.year.text;
          this.fecha.setHours(res.hour.text , res.minute.text)
        }
      }, {
        text: 'Cancelar',
        handler: () => {
          return false;
        }
      }]
    }*/
  }

  ngOnInit() {
    this.agendaService.getAllAppointments().subscribe( (data:any) => {
      this.SesionList = []

      console.log(data)
      for (const key in data) {
        if (data.hasOwnProperty(key)) {
          const element = data[key];
          let fecha = new Date(data[key].dateTime)
          var localeSpecificTime = fecha.toLocaleTimeString(navigator.language, {
            hour: '2-digit',
            minute:'2-digit'
          });

          this.SesionList.push({"numero":data[key].id,"paciente":data[key].patient.name.firstName+' '+data[key].patient.name.lastName1,"doctor":data[key].chiropractor.name.firstName+' '+data[key].chiropractor.name.lastName1,"fecha":formatDate(data[key].dateTime, 'yyyy-MM-dd', 'en-US')+' '+localeSpecificTime})

        }
      }

      console.log(data[0].chiropractor.name.firstName)
    })


    this.param='BuscarHora';
    this.columns = [
      { name: 'Paciente' },
      { name: 'Doctor' },
      { name: 'Fecha' }
    ];
    this.firebaseService.read_paciente().subscribe((data: any)  => {

    this.PacienteList = data.map(e => {
      return {
        id: e.id,
        isEdit: false,
        nombre: e.name.firstName,
        apellido: e.name.lastName1,
        edad: 15,
        profilepic: e.profilePictureRoute
      };
    })

    });

    this.firebaseService.read_doctor().subscribe((data: any) => {


    this.DoctorList = data.map(e => {
      return {
        id: e.id,
        isEdit: false,
        nombre: e.name.firstName,
        profilepic: e.profilePictureRoute,
        officeid: e.officeIds[0]
      };
    })
        console.log(this.DoctorList);
    });



    console.log(this.router.url.split('/'));
    var url=this.router.url.split('/');
    if(url.length==3){
      console.log(url[2]);
      this.param=url[2];
    }

  }

  dateSelected(_event){

    console.log(_event);

  }
  ChangeRoute(param){

    this.router.navigate(['Agenda',param]);
    this.param=param;
    this.BuscarHoraEstado=0;

  }
  ChangePage(param){
    if(this.DoctorValidado&& this.FechaValidado){
      this.BuscarHoraEstado=this.BuscarHoraEstado+param;
    }else{
      this.validarCampos();
    }
  }






  onActivate(event) {
    console.log('evento')
    if(event.type == 'click') {
      this.ModifySesion={}
      this.agendaService.getSessionData(event.row.numero).subscribe((data:any)=>{


        let fecha = new Date(data.dateTime);
        var localeSpecificTime = fecha.toLocaleTimeString(navigator.language, {
          hour: '2-digit',
          minute:'2-digit'
        });

        console.log(data);
        this.ModifySesion={estado:this.Estados[data.status],servicio:"Consulta", paciente: data.patient.id , chiropractor : data.chiropractor.id, fecha : formatDate(data.dateTime, 'yyyy-MM-dd', 'en-US'), hora: localeSpecificTime, }

        this.agendaService.getPatients().subscribe((data:any) =>{

          this.ModifyUser=data;

        });

        this.agendaService.getChiropractor().subscribe((data:any) => {

          this.ModifyChiro=data;

        })

        console.log(this.ModifySesion)
        this.BuscarHoraEstado=this.BuscarHoraEstado+1
      })

    }

  }
  presentAlertConfirm(evento) {
  this.fecha=evento;
}


  async validarCampos(){
    if(!this.DoctorValidado && !this.FechaValidado){
      const alert = await this.alertController.create({
        header: 'Debe llenar todos los campos, por favor',
        message: '',
      });

      await alert.present();

      setTimeout(()=>{
        alert.dismiss();
      }, 3000);

    }else{
  if(!this.FechaValidado){
  const alertFecha = await this.alertController.create({
    header: 'Debe seleccionar la fecha! (Fecha y hora)',
    message: '',
  });

  await alertFecha.present();

  setTimeout(()=>{
  alertFecha.dismiss();
}, 3000);
}else{
  if(!this.DoctorValidado){
  const alertDoctor = await this.alertController.create({
    header: 'Debe seleccionar al doctor',
    message: '',
  });

  await alertDoctor.present();

  setTimeout(()=>{
  alertDoctor.dismiss();
}, 3000);
}
}
}


  }
  async Hora(evento){
    let newDate = new Date(evento);
    if(!this.fecha){
      const alert2 = await this.alertController.create({
        header: 'Debe Seleccionar la fecha como primer paso',
        message: '',
      });

      await alert2.present();

      setTimeout(()=>{
      alert2.dismiss();
    }, 3000);

  }else{
    this.fecha.setHours(newDate.getHours(), newDate.getMinutes());
    const alertfecha = await this.alertController.create({
      header: 'Confirmar Fecha!',
      message: this.fecha.toLocaleString(),
      buttons: [
        {
          text: 'Cancelar',
          role: 'cancel',
          cssClass: 'secondary',
          handler: (blah) => {
            //console.log
          }
        }, {
          text: 'Aceptar',
          handler: () => {
            this.FechaValidado = true;
          }
        }
      ]
    });

    await alertfecha.present();
    let result = await alertfecha.onDidDismiss();
  }
  }


    onItemClicked(i,doctor) {
      this.currentSelected = i;
      this.doctorseleccionado = doctor.id;
      this.officeid = doctor.officeid;
      this.doctorseleccionado ? this.DoctorValidado = true : this.DoctorValidado = false ;
    }
    onPacienteClicked(b,paciente) {
      this.currentSelected2 = b;
      this.pacienteseleccionado = paciente.id;
      this.pacienteseleccionado ? this.PacienteValidado = true : this.PacienteValidado = false ;
    }
    async crearhora(){
      const alertcreacion = await this.alertController.create({
        header: 'Sesión Creada'
      });
      this.firebaseService.createSesion(this.pacienteseleccionado ,this.doctorseleccionado,this.fecha,this.officeid);
      await alertcreacion.present();

      setTimeout(()=>{
      this.ChangeRoute('CrearHora');
      alertcreacion.dismiss();

    }, 3000);



    }
}
