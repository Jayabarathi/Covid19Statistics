import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class Covid19Service {

  constructor(private http: HttpClient) { }

  getDistrictInfo(stateCode) {
    return this.http.get<any[]>('/covid19india/districts?stateCode=' + stateCode);
   }

   getStateInfo() {
     return this.http.get<any[]>('/covid19india/states');
   }

   getStateList() {
     return this.http.get<any[]>('/covid19india/stateList');
   }

   sort(states, sortReverse, columnName) {
    return states.sort((state1, state2) => {
      if(!sortReverse){
        return state2[columnName] - state1[columnName];
      } else {
        return state1[columnName] - state2[columnName];
      }
    });
   }
}
