import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { StateInfoComponent } from './component/state-info/state-info.component';
import { DistrictInfoComponent } from './component/district-info/district-info.component';


const routes: Routes = [
  { path: '', redirectTo: 'stateInfo', pathMatch: 'full' },
  { path: 'stateInfo', component: StateInfoComponent },
  { path: 'districtInfo/:stateCode', component: DistrictInfoComponent },
  { path: '**', component: StateInfoComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
export const routingComponents = [StateInfoComponent, DistrictInfoComponent]
