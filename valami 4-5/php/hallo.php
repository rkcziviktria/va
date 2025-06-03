<?php
header('Content-Type: application/json');

// Adatbázis kapcsolódási paraméterek
$servername = "localhost";
$username = "root"; // Az adatbázis felhasználóneved
$password = "";     // Az adatbázis jelszavad
$dbname = "your_database_name"; // Cseréld ki az adatbázisod nevére


$conn = new mysqli($servername, $username, $password, $dbname);


if ($conn->connect_error) {
    die(json_encode(["hiba" => "Adatbázis kapcsolódási hiba: " . $conn->connect_error]));
}


$varos = isset($_GET['varos']) ? $_GET['varos'] : null;
$datum = isset($_GET['datum']) ? $_GET['datum'] : null;

$sql = "SELECT varos, datum, homerseklet, paratartalom, szelsebesseg FROM idojaras";
$where_clauses = [];
$params = [];
$types = '';

if ($varos) {
    $where_clauses[] = "varos = ?";
    $params[] = $varos;
    $types .= 's';
}

if ($datum) {
   
    if (!preg_match("/^\d{4}-\d{2}-\d{2}$/", $datum)) {
        echo json_encode(["hiba" => "Érvénytelen dátum formátum. Használja az ÉÉÉÉ-HH-NN formátumot."]);
        $conn->close();
        exit();
    }
    $where_clauses[] = "datum = ?";
    $params[] = $datum;
    $types .= 's';
}

if (count($where_clauses) > 0) {
    $sql .= " WHERE " . implode(" AND ", $where_clauses);
}


if (!$varos && !$datum) {
   
    echo json_encode([
        "minta" => [
            "/api/idojaras/?varos=Debrecen&datum=2024-02-15",
            "/api/idojaras/?varos=Szeged",
            "/api/idojaras/?varos=Budapest&datum=2024-03-02"
        ]
    ]);
    $conn->close();
    exit();
}



$stmt = $conn->prepare($sql);
if ($stmt === false) {
    echo json_encode(["hiba" => "SQL előkészítési hiba: " . $conn->error]);
    $conn->close();
    exit();
}

if (!empty($params)) {
    $stmt->bind_param($types, ...$params);
}

$stmt->execute();
$result = $stmt->get_result();

$weather_data = [];
if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
       
        $weather_data[] = [
            "varos" => $row['varos'],
            "homerseklet" => $row['homerseklet'],
            "paratartalom" => $row['paratartalom'],
            "szelsebesseg" => $row['szelsebesseg']
            
        ];
    }
    echo json_encode($weather_data);
} else {
   
    echo json_encode(["hiba" => "nincs találat"]);
}

$stmt->close();
$conn->close();
?>