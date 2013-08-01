/**
 *  weightperagents
 *  Author: Martine
 *  Description: 
 */

model weightperagents

global {
	map<road, float> roads_weight;
	graph road_network;
	float slow_coeff <- 3.0;
	init {
		create road {
			shape <- line ([{10,50},{90,50}]);
			slow <- true;
		}
		create road {
			shape <- line ([{10,50},{10,10}]);
			slow <- false;
		}
		create road {
			shape <- line ([{10,10},{90,10}]);
			slow <- false;
		}
		create road {
			shape <- line ([{90,10},{90,50}]);
			slow <- false;
		}
		roads_weight <- road as_map (each:: each.shape.perimeter * (each.slow ? slow_coeff : 1.0));
		road_network <- as_edge_graph(road);
		
		//people with information about the traffic
		create people {
			color <- rgb("blue");
			size <- 2;
			roads_knowledge <- road as_map (each:: each.shape.perimeter * (each.slow ? slow_coeff : 1.0));
		}
		
		//people without information about the traffic
		create people {
			color <- rgb("yellow");
			size <- 1;
			roads_knowledge <- road as_map (each:: each.shape.perimeter);
		}
	}
	
}

entities {
	species road {
		bool slow;
		aspect geom {
			draw shape color: slow ? rgb("red") : rgb("green");
		}
	}
	
	species people skills: [moving] {
		map<road, float> roads_knowledge;
		point the_target;
		rgb color;
		float size;
		path path_to_follow;
		init {
			the_target <- {90,50};
			location <- {10,50};
		}
		
		reflex movement when: location != the_target{
			if (path_to_follow = nil) {
				path_to_follow <- (road_network with_weights roads_knowledge) path_between (location::the_target);
			}
			do follow path:path_to_follow speed: 5 move_weights: roads_weight;
		}
		
		aspect base {
			draw circle(size) color: color;
		}
	}
}

experiment weightperagents type: gui {
	output {
		display map {
			species road aspect: geom;
			species people aspect: base;
		}
	}
}
