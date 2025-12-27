import { View, StyleSheet, ScrollView } from 'react-native';
import { useEffect, useState } from 'react';
import { createGrid } from '../../utils/createGrid';
import { Cell, CellType } from '../../types/grid';
import Grid from '../../components/Grid';

export default function HomeScreen() {
  const [grid, setGrid] = useState<Cell[][]>([]);
  const [startSet, setStartSet] = useState(false);
  const [endSet, setEndSet] = useState(false);
  const [isPressing, setIsPressing] = useState(false);

  useEffect(() => {
    setGrid(createGrid());
  }, []);

  const updateCell = (cell: Cell, newType: CellType) => {
    setGrid(prev =>
      prev.map(row =>
        row.map(c =>
          c.row === cell.row && c.col === cell.col
            ? { ...c, type: newType }
            : c
        )
      )
    );
  };

  const handleCellPress = (cell: Cell) => {
    if (!startSet) {
      updateCell(cell, 'start');
      setStartSet(true);
      return;
    }

    if (!endSet) {
      updateCell(cell, 'end');
      setEndSet(true);
      return;
    }

    // After start & end are set → obstacles
    if (cell.type === 'empty') {
      updateCell(cell, 'obstacle');
    }
  };

  return (
    <View style={styles.container}>
      <ScrollView horizontal>
        <ScrollView>
          <Grid
            grid={grid}
            onCellPress={handleCellPress}
            onPressIn={() => setIsPressing(true)}
            onPressOut={() => setIsPressing(false)}
            isPressing={isPressing}
          />
        </ScrollView>
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#05010a',
    paddingTop: 20,
  },
});