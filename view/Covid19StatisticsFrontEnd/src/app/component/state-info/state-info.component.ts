import { Component, OnInit } from '@angular/core';
import { Covid19Service } from 'src/app/services/covid19.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-state-info',
  templateUrl: './state-info.component.html',
  styleUrls: ['./state-info.component.css'],
  
})
export class StateInfoComponent implements OnInit {

  states: any;
  currentState: null;
  currentIndex: -1;
  countryName = '';
  countryActive = '';
  countryConfirmed = '';
  countryRecovered = '';
  countryDeceased = '';
  showLoader:Boolean = false;
  sortConfirmed:Boolean = false;
  sortActive:Boolean = false;
  sortDeceased:Boolean = false;
  sortRecovered:Boolean = false;
  columns = ['confirmed', 'active', 'deceased', 'recovered'];


  constructor(private covid19service: Covid19Service, 
    private router: Router) { }

  ngOnInit(): void {
    this.retrieveStates();
  }

  retrieveStates() {
    this.covid19service.getStateInfo()
    .subscribe(
      data => {
        this.states = data['stateList'];
        this.countryName = data['countryName'];
        this.countryConfirmed = data['countryConfirmed'];
        this.countryActive = data['countryActive'];
        this.countryDeceased = data['countryDeceased'];
        this.countryRecovered = data['countryRecovered'];
        this.showLoader = true;
      },
      error => {
        console.log(error);
      });
  }

  setCurrentState(state, index) {
    console.log(state.statecode + ' ' + index);
    this.currentState = state;
    this.currentIndex = index;
  }

  viewDistrictInfo(state) {
    this.router.navigate(['districtInfo', state.statecode]);
  }

  sortConfirm(sortConfirmed, index){
    this.sortConfirmed = sortConfirmed;
    this.covid19service.sort(this.states, sortConfirmed, this.columns[index]);
  }

  sortActives(sortActive, index){
    this.sortActive = sortActive;
    this.covid19service.sort(this.states, sortActive, this.columns[index]);
  }

  sortDeceas(sortDeceased, index){
    this.sortDeceased = sortDeceased;
    this.covid19service.sort(this.states, sortDeceased, this.columns[index]);
  }

  sortRecover(sortRecovered, index){
    this.sortRecovered = sortRecovered;
    this.covid19service.sort(this.states, sortRecovered, this.columns[index]);
  }
}
