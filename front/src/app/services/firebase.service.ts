import { Injectable } from '@angular/core';
import { AngularFirestore } from '@angular/fire/firestore';
import { HttpClient} from '@angular/common/http';
import {Subject} from 'rxjs';
import { AuthenticationService } from '../shared/authentication-service';
import { User } from '../shared/user';

@Injectable({
  providedIn: 'root'
})
export class FirebaseService {
  private fooSubject = new Subject<any>();
  collectionName = 'Pacientes';
  collectionName2 = 'Doctor';
  collectionName3 = 'Sesion';

  constructor(
    private firestore: AngularFirestore,
    public httpClient: HttpClient
  ) { }

  create_paciente(record) {
    return this.firestore.collection(this.collectionName).add(record);
  }

  read_paciente() {
    return this.httpClient.get("http://localhost:8091/patients/");
  }
  read_doctor() {
    return this.httpClient.get("http://localhost:8091/offices/1/chiropractors");
  }
  doctor_detail(id){
    return this.firestore.collection(this.collectionName2).doc(id);
  }
  paciente_detail(id){
    return this.firestore.collection(this.collectionName).doc(id)['Nombre'];
  }
  read_sesion() {
    return this.firestore.collection(this.collectionName3).snapshotChanges();
  }

  update_paciente(recordID, record) {
    this.firestore.doc(this.collectionName + '/' + recordID).update(record);
  }

  delete_paciente(record_id) {
    this.firestore.doc(this.collectionName + '/' + record_id).delete();
  }
  createSesion(
      paciente,
      doctor,
      fecha: Date,
      officeid
    ): Promise<void> {
      let postData = {
        "typeId": 1,
        "dateTime": fecha,
        "officeId": officeid,
        "patientId": paciente,
        "chiropractorId": doctor
   }

   this.httpClient.post("http://localhost:8091/appointments/create", postData)
     .subscribe(data => {
       console.log(data['_body']);
      }, error => {
       console.log(error);
     });


      const id = this.firestore.createId();

      return this.firestore.doc(this.collectionName3 + '/' + id).set({
        paciente,
        doctor,
        fecha
      });
    }


    publishSomeData(data: any) {
        this.fooSubject.next(data);
    }

    getObservable(): Subject<any> {
        return this.fooSubject;
    }
}
