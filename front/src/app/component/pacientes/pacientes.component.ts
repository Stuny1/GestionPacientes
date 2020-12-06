import { Component, OnInit } from '@angular/core';
import { ViewEncapsulation } from '@angular/core';
import { FirebaseService } from '../../services/firebase.service';

@Component({
  selector: 'app-pacientes',
  templateUrl: './pacientes.component.html',
  styleUrls: ['./pacientes.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class PacientesComponent implements OnInit {
  public columns=[
    { name: 'Nombre', prop: 'name' },
    { name: 'Apellido', prop:'gender' },
    { name: 'Edad', prop:'age' }
  ];
  public pacienteEstado=0;
  public PacienteMod=0;
  public rows: any;

  constructor() { }

  ngOnInit() {

    this.rows = [
      {
        "name": "Ethel Price",
        "gender": "female",
        "age": 22
      },
      {
        "name": "Claudine Neal",
        "gender": "female",
        "age": 55
      },
      {
        "name": "Beryl Rice",
        "gender": "female",
        "age": 67
      },
      {
        "name": "Simon Grimm",
        "gender": "male",
        "age": 28
      }
    ];

  }

  ChangePage(param){

    this.pacienteEstado=this.pacienteEstado+param;

  }

  changeModState(param){

    this.PacienteMod=param;

  }

}
