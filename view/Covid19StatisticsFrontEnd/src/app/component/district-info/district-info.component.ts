import { Component, OnInit } from '@angular/core';
import { Covid19Service } from 'src/app/services/covid19.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-district-info',
  templateUrl: './district-info.component.html',
  styleUrls: ['./district-info.component.css']
})
export class DistrictInfoComponent implements OnInit {

  districts: any;
  stateActive = '';
  stateConfirmed = '';
  stateDeceased = '';
  stateRecovered = '';
  states: any = [];
  selectedState: any;
  showLoader:Boolean = false;
  sortConfirmed:Boolean = false;
  sortActive:Boolean = false;
  sortDeceased:Boolean = false;
  sortRecovered:Boolean = false;
  columns = ['confirmed', 'active', 'deceased', 'recovered'];

  constructor(private covid19service: Covid19Service,
    private route: ActivatedRoute,
    private router: Router) { }

  ngOnInit(): void {
    this.selectedState = this.route.snapshot.paramMap.get('stateCode');
    this.retrieveDistricts(this.selectedState, true);
    this.getStateList();
  }

  retrieveDistricts(stateCode, comboFlag) {
    this.covid19service.getDistrictInfo(stateCode)
    .subscribe(
      data => {
        this.districts = data['districtList'];
        this.stateActive = data['stateActive'];
        this.stateRecovered = data['stateRecovered'];
        this.stateConfirmed = data['stateConfirmed'];
        this.stateDeceased = data['stateDeceased'];
        this.showLoader = true;
      },
      error => {
        console.log(error);
      });
  }

  changeStateName(stateCode){
    this.selectedState = stateCode;
    this.router.navigate(['districtInfo', stateCode]);
    this.retrieveDistricts(stateCode, false);
  }

  getStateList(){
    this.covid19service.getStateList()
    .subscribe(
      data => {
        this.states = data;
        this.showLoader = true;
      },
      error => {
        console.log(error);
      });
  }

  sortConfirm(sortConfirmed, index){
    this.sortConfirmed = sortConfirmed;
    this.covid19service.sort(this.districts, sortConfirmed, this.columns[index]);
  }

  sortActives(sortActive, index){
    this.sortActive = sortActive;
    this.covid19service.sort(this.districts, sortActive, this.columns[index]);
  }

  sortDeceas(sortDeceased, index){
    this.sortDeceased = sortDeceased;
    this.covid19service.sort(this.districts, sortDeceased, this.columns[index]);
  }

  sortRecover(sortRecovered, index){
    this.sortRecovered = sortRecovered;
    this.covid19service.sort(this.districts, sortRecovered, this.columns[index]);
  }

}
