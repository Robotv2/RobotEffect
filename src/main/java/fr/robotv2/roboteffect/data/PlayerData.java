package fr.robotv2.roboteffect.data;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DatabaseTable(tableName = "robot_effect_data")
public class PlayerData {
    @DatabaseField(columnName = "uuid", unique = true, id = true)
    private UUID uuid;
    @DatabaseField(columnName = "effects", dataType = DataType.SERIALIZABLE)
    private HashSet<String> effects;
}
