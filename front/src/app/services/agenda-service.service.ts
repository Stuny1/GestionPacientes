import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';

const httpOptions = {
  headers: new HttpHeaders({ 
    'Access-Control-Allow-Origin':'*'
  })
};


@Injectable({
  providedIn: 'root'
})


export class AgendaServiceService {

  public API_URL= environment.apiUrl;

  

  constructor(private httpClient: HttpClient) { 
  }

  public getAllAppointments(){
    //ToDo: Cambiar parametro dinamico de officeId
    return this.httpClient.post(this.API_URL+'/appointments/', {"officeId": 1});
  }
  public getSessionData(id){

    return this.httpClient.get(this.API_URL+'/appointments/'+id);

  }

  public getPatients(){

    return this.httpClient.get(this.API_URL+'/patients/');

  }

  public getChiropractor(param=1){

    return this.httpClient.get(this.API_URL+'/offices/'+param+'/chiropractors');

  }
}
