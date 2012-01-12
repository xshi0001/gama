model prey_predator

global {
	var nb_preys_init type: int init: 200 min: 1 max: 1000 parameter: 'Initial number of preys: ' category: 'Prey' ;
	var nb_predator_init type: int init: 20 min: 0 max: 200 parameter: 'Initial number of predators ' category: 'Predator' ;
	var prey_max_energy type: float init: 1 parameter: 'Prey max energy: ' category: 'Prey' ;
	var prey_max_transfert type: float init: 0.1 parameter: 'Prey max transfert: ' category: 'Prey' ;
	var prey_energy_consum type: float init: 0.05 parameter: 'Prey energy consumption: ' category: 'Prey' ;
	var predator_max_energy type: float init: 1 parameter: 'Predator max energy: ' category: 'Predator' ;
	var predator_energy_transfert type: float init: 0.5 parameter: 'Predator energy transfert: ' category: 'Predator' ;
	var predator_energy_consum type: float init: 0.02 parameter: 'Predator energy consumption: ' category: 'Predator' ;
	var nb_preys type: int value: length (prey as list) init: nb_preys_init ;
	var nb_predators type: int value: length (predator as list) init: nb_predator_init ;
	init {
		create species: prey number: nb_preys_init ;
		create species: predator number: nb_predators ;
	}
}
entities {
	species generic_species {
		const size type: float init: 2 ;
		const color type: rgb;
		const max_energy type: float;
		const energy_consum type: float;
		var myCell type: the_grid init: one_of (the_grid as list) ;
		var energy type: float init: (rnd(1000) / 1000) * max_energy  value: energy - energy_consum max: max_energy ;
		init {
			set location value: myCell.location ;
			set energy value: (rnd(1000) / 1000) * max_energy ;
		}
		reflex basic_move {
			set myCell value: one_of (myCell.neighbours) ;
			set location value: myCell.location ;
		}
		reflex die when: energy <= 0 {
			do action: die ;
		}
		aspect base {
			draw shape: circle size: size color: color ;
		}
	}
	species prey parent: generic_species {
		const color type: rgb init: 'blue' ;
		const max_energy type: float init: prey_max_energy ;
		const max_transfert type: float init: prey_max_transfert ;
		const energy_consum type: float init: prey_energy_consum ;
		reflex eat when: myCell.food > 0 {
			let energy_transfert value: min [max_transfert, myCell.food] ;
			set myCell.food value: myCell.food - energy_transfert ;
			set energy value: energy + energy_transfert ;
		}
	}
	species predator parent: generic_species {
		const color type: rgb init: 'red' ;
		const max_energy type: float init: predator_max_energy ;
		const energy_transfert type: float init: predator_energy_transfert ;
		const energy_consum type: float init: predator_energy_consum ;
		reflex eat when: !(empty (agents_inside(myCell) of_species prey)) {
			ask target: one_of (agents_inside(myCell) of_species prey) {
				do action: die ;
			}
			set energy value: energy + energy_transfert ;
		}
	}
}
environment width: 100 height: 100 {
	grid the_grid width: 50 height: 50 neighbours: 4 {
		const maxFood type: float init: 1.0 ;
		const foodProd type: float init: (rnd(1000) / 1000) * 0.01 ;
		var food type: float init: (rnd(1000) / 1000) value: min [maxFood, food + foodProd] ;
		var color type: rgb value: [255 * (1 - food), 255, 255 * (1 - food)] init: [255 * (1 - food), 255, 255 * (1 - food)] ;
		var neighbours type: list of: the_grid init: (self neighbours_at 2) of_species the_grid;
	}
}
output {
	display main_display {
		grid the_grid lines: 'black' ;
		species prey aspect: base ;
		species predator aspect: base ;
	}
	monitor number_of_preys value: nb_preys refresh_every: 1 ;
	monitor number_of_predators value: nb_predators refresh_every: 1 ;
}
