import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient,HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class StatisticsService {
  public API_URL= environment.apiUrl;
  constructor(private httpClient: HttpClient) { }

  public getAllStatics(){
    //ToDo: Cambiar parametro dinamico de category
    return this.httpClient.post(this.API_URL+'/offices/1/statistics', {"category": "all"});
  }

}
