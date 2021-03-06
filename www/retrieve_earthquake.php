    <?php
				// Recupero los datos del servicio sismologico y retorno en formato JSON
				$my_url = 'http://www.sismologia.cl/links/ultimos_sismos.html';
				
				$html = file_get_contents ( $my_url );
				$dom = new DOMDocument ();
				$dom->loadHTML ( $html );
				$xpath = new DOMXPath ( $dom );
				
				// Put your XPath Query here
				$my_xpath_query = "/html/body//tr";
				$result_rows = $xpath->query ( $my_xpath_query );
				
				$earthquakes = array ();
				
				// here we loop through our results (a DOMDocument Object)
				$i = 0;
				foreach ( $result_rows as $result_object ) {
					$earthquake = array ();
					
					if ($i > 0) {
						foreach ( $result_object->childNodes as $node ) {
							
							array_push ( $earthquake, $node->nodeValue );
						}
						
						array_push ( $earthquakes, $earthquake );
					
					}
					$i ++;
					
					
					
					// echo $result_object->childNodes->item(0)->nodeValue . "<br>";
				}
				
				echo json_encode ( array (
						'earthquakes' => $earthquakes 
				) );
				
				?>